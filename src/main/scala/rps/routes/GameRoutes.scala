package rps

// import cats._
import cats.implicits._
import cats.effect._
import org.http4s._
// import org.http4s.server._
import org.http4s.dsl.Http4sDsl
// import scala.concurrent.ExecutionContext
// import io.chrisdavenport.log4cats.Logger
import io.circe.generic.JsonCodec
import io.buildo.enumero.circe._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._

@JsonCodec case class PlayRequest(userMove: Move)

class GameRoutes[F[_]: Sync](gameService: GameService[F]) extends Http4sDsl[F] {

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "play" =>
      for {
        playRequest <- req.as[PlayRequest]
        result <- gameService.play(playRequest.userMove)
        res <- Ok(result)
      } yield res
  }

  val routes: HttpRoutes[F] = httpRoutes
}
