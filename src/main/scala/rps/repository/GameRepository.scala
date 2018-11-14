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
  import DbGameRepositoryImpl._

  override def list(): F[List[Game]] =
    listQuery.to[List].transact(xa)

  override def store(game: Game): F[Id[Game]] =
    for {
      uuid <- FUUID.randomFUUID[F]
      _ <- storeUpdate(uuid, game).run.transact(xa)
    } yield Id(uuid)

}

object DbGameRepositoryImpl {
  import DoobieInstances._

  val listQuery = sql"select userMove, cpuMove, result from game".query[Game]

  def storeUpdate(uuid: FUUID, game: Game) = sql"""
    insert into game
    (id, userMove, cpuMove, result) values
    ($uuid, ${game.userMove}, ${game.cpuMove}, ${game.result})
  """.update
}
