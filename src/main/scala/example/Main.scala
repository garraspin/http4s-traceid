package example

import java.util.concurrent.{ExecutorService, Executors, ScheduledExecutorService}

import cats.effect.{ContextShift, ExitCode, IO, IOApp, Timer}
import cats.implicits._
import kamon.Kamon
import kamon.http4s.middleware._
import kamon.instrumentation.executor.ExecutorInstrumentation
import kamon.instrumentation.executor.ExecutorInstrumentation.Settings
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.HttpRoutes
import org.slf4j.LoggerFactory
import org.http4s.implicits._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object Main extends IOApp {
  Kamon.init()
  private val port = 9000
  private val host = "0.0.0.0"
  private lazy val logger = LoggerFactory.getLogger(this.getClass)

  val scheduledES: ScheduledExecutorService = ExecutorInstrumentation.instrumentScheduledExecutor(
    Executors.newScheduledThreadPool(1),
    "globalScheduled"
  )

  private val global: ExecutorService = ExecutorInstrumentation.instrument(
    Executors.newFixedThreadPool(1),
    "global",
    new Settings(shouldTrackTimeInQueue = true, shouldPropagateContextOnSubmit = true)
  )
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    global
  )
  override implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)
  override implicit val timer: Timer[IO] = IO.timer(ec, scheduledES)

  override def run(args: List[String]): IO[ExitCode] = {
    val router = Router(
      "/" -> HttpRoutes.of[IO] {
        case GET -> Root / "foo" =>
          for {
            _ <- IO(logger.info(s"Server: before ${Kamon.currentSpan().trace.id.string}"))
            _ <- IO.sleep(1000.millis)
            _ <- IO(logger.info(s"Server: after ${Kamon.currentSpan().trace.id.string}"))
            r <- Ok(s"GET")
          } yield r
      }
    )

    for {
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(port, host)
        .withExecutionContext(ec)
        .withHttpApp(server.KamonSupport[IO](router, host, port).orNotFound)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    } yield exitCode
  }

}
