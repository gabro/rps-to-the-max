package rps

import io.circe.generic.JsonCodec
import io.buildo.enumero.circe._

@JsonCodec case class Game(userMove: Move, cpuMove: Move, result: Result)
