import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val http4s = Seq(
    "org.http4s"                       %% "http4s-dsl"                  % "0.18.0",
    "org.http4s"                       %% "http4s-blaze-server"         % "0.18.0",
    "org.http4s"                       %% "http4s-blaze-client"         % "0.18.0",
    "org.http4s"                       %% "http4s-circe"                % "0.18.0",
    "io.circe"                         %% "circe-generic"               % "0.9.1"
  )
  lazy val logging = Seq(
    "ch.qos.logback"                   %  "logback-classic"             % "1.2.3" % Runtime
  )

  lazy val monitoring = Seq(
    "io.kamon"                         %% "kamon-core"                  % "1.1.0",
    "io.kamon"                         %% "kamon-http4s"                % "1.0.4",
    "io.kamon"                         %% "kamon-executors"             % "1.0.1",
    "io.kamon"                         %% "kamon-logback"               % "1.0.0",
    "io.kamon"                         %% "kamon-scala-future"          % "1.0.0"
  )
}
