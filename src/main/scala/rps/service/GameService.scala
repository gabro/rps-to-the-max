package rps

import cats.effect._
import cats.implicits._
import scala.util.Random

trait GameService[F[_]] {
  def play(userMove: Move): F[Game]
}

class GameServiceImpl[F[_]](implicit F: Sync[F]) extends GameService[F] {
  override def play(userMove: Move): F[Game] =
    for {
      cpuMove <- generateMove()
      result <- computeResult(userMove, cpuMove)
    } yield Game(userMove, cpuMove, result)

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
