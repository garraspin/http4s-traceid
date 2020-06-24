import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val http4sVersion = "0.21.4"
  lazy val kamonBundle = "2.1.1"
  lazy val http4s = Seq(
    "org.http4s"                       %% "http4s-dsl"                  % http4sVersion,
    "org.http4s"                       %% "http4s-blaze-server"         % http4sVersion,
    "org.http4s"                       %% "http4s-blaze-client"         % http4sVersion,
    "org.http4s"                       %% "http4s-circe"                % http4sVersion,
    "io.circe"                         %% "circe-generic"               % "0.13.0"
  )
  lazy val logging = Seq(
    "ch.qos.logback"                   %  "logback-classic"             % "1.2.3" % Runtime
  )

  lazy val monitoring = Seq(
    "io.kamon"                         %% "kamon-bundle"                % kamonBundle,
    "io.kamon"                         %% "kamon-scala-future"          % kamonBundle,
    "io.kamon"                         %% "kamon-cats-io"               % kamonBundle,
    "io.kamon"                         %% "kamon-http4s"                % "2.0.3"
  )
}
