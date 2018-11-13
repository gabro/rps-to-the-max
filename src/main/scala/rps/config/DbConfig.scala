package rps

case class DbConfig(
    database: String,
    user: String,
    password: String
) {
  lazy val url = s"jdbc:postgresql:$database"
}
