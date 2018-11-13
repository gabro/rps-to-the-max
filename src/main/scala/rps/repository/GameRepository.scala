package rps

import cats.implicits._
import cats.effect._
import io.chrisdavenport.fuuid.FUUID
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect._
import doobie._
import doobie.implicits._

import scala.collection.JavaConverters._

trait GameRepository[F[_]] {
  def store(game: Game): F[Id[Game]]
  def list(): F[List[Game]]
}

class InMemoryGameRepositoryImpl[F[_]](implicit F: Sync[F]) extends GameRepository[F] {
  private val storage =
    new java.util.concurrent.ConcurrentHashMap[Id[Game], Game]

  override def list(): F[List[Game]] =
    F.delay(storage.values.asScala.toList)

  override def store(game: Game): F[Id[Game]] =
    for {
      uuid <- FUUID.randomFUUID[F]
      id = Id[Game](uuid)
      _ <- F.delay(storage.put(id, game))
    } yield id
}

class DbGameRepositoryImpl[F[_]: Async: ContextShift](xa: Transactor[F]) extends GameRepository[F] {

  import doobie.postgres.implicits._
  import io.chrisdavenport.fuuid.doobie.implicits._
  import io.buildo.enumero._
  implicit def enumeroMeta[E <: CaseEnum](implicit s: CaseEnumSerialization[E]): Meta[E] =
    Meta[String].imap(s.caseFromString(_).get)(s.caseToString)

  override def list(): F[List[Game]] =
    sql"select userMove, cpuMove, result from game".query[Game].to[List].transact(xa)

  override def store(game: Game): F[Id[Game]] =
    for {
      uuid <- FUUID.randomFUUID[F]
      _ <- sql"""insert into game
          (id, userMove, cpuMove, result) values
          ($uuid, ${game.userMove}::move, ${game.cpuMove}::move, ${game.result}::result)
        """.update.run.transact(xa)
    } yield Id(uuid)

}
