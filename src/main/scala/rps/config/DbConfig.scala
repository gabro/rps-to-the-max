package rps

case class DbConfig(
    host: String,
    port: Int,
    database: String,
    user: String,
    password: String
) {
  lazy val url = s"jdbc:postgresql://$host:$port/$database"
}
