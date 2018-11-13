package rps

case class FlywayConfig(
    mockedData: Boolean,
    clean: Boolean,
    validate: Boolean,
    migrate: Boolean
)
