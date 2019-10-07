package example

import java.util.concurrent.Executors

import cats.effect.{ContextShift, ExitCode, IO, IOApp, Timer}
import cats.implicits._
import kamon.Kamon
import kamon.http4s.middleware._
import kamon.instrumentation.executor.ExecutorInstrumentation
import kamon.instrumentation.executor.ExecutorInstrumentation.Settings
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpRoutes, Request, Uri}
import org.slf4j.LoggerFactory
import org.http4s.implicits._

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  Kamon.init()
  private val port = 9000
  private val host = "0.0.0.0"
  private lazy val logger = LoggerFactory.getLogger(this.getClass)

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    ExecutorInstrumentation.instrument(
      Executors.newFixedThreadPool(10),
      "global",
      new Settings(shouldTrackTimeInQueue = true, shouldPropagateContextOnSubmit = true)
    )
  )
  override implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)
  override implicit val timer: Timer[IO] = IO.timer(ec)

  override def run(args: List[String]): IO[ExitCode] = BlazeClientBuilder[IO](ec).resource.use { http4sclient =>
    val cli = client.KamonSupport(http4sclient)
    val router = Router(
      "/" -> HttpRoutes.of[IO] {
        case GET -> Root / "bar" / traceId =>
          logger.info(s"Client: called with traceId $traceId and current is $currentTraceId")
          //          Thread.sleep(500)
          Ok(traceId)
        case GET -> Root / "foo" =>
          for {
            _ <- IO(logger.info(s"Server: calling client with $currentTraceId"))
            _ <- cli.expect[String](Request[IO](GET, Uri.unsafeFromString(s"http://localhost:9000/bar/$currentTraceId")))
              .attempt
              .map { clientResponse =>
                logger.info(s"Server: client returned: $clientResponse and current is $currentTraceId")
                clientResponse
              }
            r <- Ok(s"GET")
          } yield r
      }
    )

    val configuredHttpService = server.KamonSupport[IO](router, host, port).orNotFound
    for {
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(port, host)
        .withExecutionContext(ec) //<-----------------------  IMPORTANT
        .withHttpApp(configuredHttpService)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    } yield exitCode
  }

  private def currentTraceId: String = Kamon.currentSpan().trace.id.string
}
