package rps

import io.buildo.enumero.annotations.enum

@enum trait Result {
  object UserWin
  object CpuWin
  object Draw
}
