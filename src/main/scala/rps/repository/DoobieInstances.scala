package rps

import doobie.postgres.implicits._
import io.chrisdavenport.fuuid.FUUID
import io.buildo.enumero._
import doobie._
import java.util.UUID
import scala.reflect.runtime.universe.TypeTag

object DoobieInstances {

  implicit def enumeroMeta[E <: CaseEnum: TypeTag](implicit s: CaseEnumSerialization[E]): Meta[E] =
    pgEnumStringOpt(s.name, s.caseFromString, s.caseToString)

  implicit val fuuidMeta: Meta[FUUID] =
    Meta[UUID].timap[FUUID](FUUID.fromUUID)(fuuid => FUUID.Unsafe.toUUID(fuuid))

}
