package rps

import cats.effect._
import cats.implicits._
import scala.util.Random
import io.chrisdavenport.log4cats.Logger

trait GameService[F[_]] {
  def play(userMove: Move): F[Game]
  def list(): F[List[Game]]
}

class GameServiceImpl[F[_]](gameRepository: GameRepository[F])(
    implicit F: Sync[F],
    logger: Logger[F])
    extends GameService[F] {

  override def play(userMove: Move): F[Game] =
    for {
      cpuMove <- generateMove()
      result <- computeResult(userMove, cpuMove)
      _ <- logger.info(s"$userMove vs $cpuMove: $result")
      game = Game(userMove, cpuMove, result)
      _ <- gameRepository.store(game)
    } yield game

  override def list(): F[List[Game]] =
    gameRepository.list()

  private def generateMove(): F[Move] =
    F.delay(Random.shuffle(Move.values.toList).head)

  private def computeResult(userMove: Move, cpuMove: Move): F[Result] =
    F.delay {
      import Move._
      (userMove, cpuMove) match {
        case (x, y) if x == y => Result.Draw
        case (Paper, Rock) | (Rock, Scissors) | (Scissors, Paper) =>
          Result.UserWin
        case _ => Result.CpuWin
      }
    }
}
