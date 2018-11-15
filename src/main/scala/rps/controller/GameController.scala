package rps

class query extends scala.annotation.StaticAnnotation
class command extends scala.annotation.StaticAnnotation

trait GameController[F[_]] {

  @query
  def list(): F[List[Game]]

  @command
  def play(userMove: Move, result: Result): F[Game]

}

class GameControllerImpl[F[_]](gameService: GameService[F]) extends GameController[F] {

  def list(): F[List[Game]] = gameService.list()

  def play(userMove: Move, result: Result): F[Game] = gameService.play(userMove)
}
