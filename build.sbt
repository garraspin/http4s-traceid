import Dependencies._

lazy val root = (project in file("."))
.enablePlugins(JavaAgent)
.settings(javaAgents += "org.aspectj" % "aspectjweaver" % "1.8.11"  % "runtime")
.settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "http4s-traceid",
    libraryDependencies ++= (http4s ++ logging ++ monitoring)
  )
