package rps

import cats.implicits._
import cats.effect._
import org.http4s.server._
import org.http4s.server.blaze._
import org.http4s.implicits._
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect._

object Boot extends IOApp {

  val gameService = new GameServiceImpl[IO]
  val gameRoutes = new GameRoutes(gameService)

  val service = Router(
    "/rps" -> gameRoutes.routes
  ).orNotFound

  val httpServer =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

  def run(args: List[String]): IO[ExitCode] =
    for {
      config <- loadConfigF[IO, ServerConfig]
      server <- httpServer
    } yield server

}
