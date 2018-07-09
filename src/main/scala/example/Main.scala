package example

import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, Executor, Executors, TimeUnit}

import Main.logger
import cats.effect.IO
import cats.instances.list._
import cats.syntax.traverse._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.executors.util.ContextAwareExecutorService
import kamon.http4s.middleware._
import org.http4s.client.blaze.{BlazeClientConfig, Http1Client}
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.{Headers, HttpService, Request, Uri}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, ExecutionContextExecutorService}

case class Ping(msg: String)
case class Pong(msg: String)

trait Logging {
  lazy val logger = LoggerFactory.getLogger(this.getClass)
}

object Main extends StreamApp[IO] with Logging {
  val port = 9000

  object ExecutionContextExecutorServiceBridge {
    def apply(ec: ExecutionContext): ExecutionContextExecutorService = ec match {
      case null => throw null
      case eces: ExecutionContextExecutorService => eces
      case other => new AbstractExecutorService with ExecutionContextExecutorService {
        override def prepare(): ExecutionContext = other
        override def isShutdown = false
        override def isTerminated = false
        override def shutdown() = ()
        override def shutdownNow() = Collections.emptyList[Runnable]
        override def execute(runnable: Runnable): Unit = other execute runnable
        override def reportFailure(t: Throwable): Unit = other reportFailure t
        override def awaitTermination(length: Long,unit: TimeUnit): Boolean = false
      }
    }
  }

  val tp = Executors.newScheduledThreadPool(10) //added just so that the thread would be named something else than Execution-Context-Global
  val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    ContextAwareExecutorService(ExecutionContextExecutorServiceBridge(ExecutionContext.fromExecutor((command: Runnable) => tp.submit(command))))
  )
  implicit val implicitEc: ExecutionContextExecutor = ExecutionContext.global //this is not really used when handling requests 

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    for {
      http4sclient <- Http1Client.stream[IO](BlazeClientConfig.defaultConfig.copy(executionContext = ec)) //<---------  IMPORTANT
      client        = kamon.http4s.middleware.client.KamonSupport(http4sclient)
      router  = Router(
        "/" -> HttpService[IO] {
          case GET -> Root / "foo" =>
            for {
              _ <- (1 to 100).toList.traverse[IO, String]( _ => {
                client
                  .expect[String](Request[IO](method = GET, uri = Uri.unsafeFromString(s"http://localhost:10000/bar"), headers = Headers.empty))
                  .attempt
                  .map(rr => {
                    logger.info(s"The call was made and returned an error: ${rr}")
                    ""
                  })
              })
              r   <- Ok(s"GET")
            } yield r
        }
      )

      exitCode <- BlazeBuilder[IO]
        .bindHttp(port, "0.0.0.0")
        .withExecutionContext(ec) //<-----------------------  IMPORTANT
        .mountService(server.KamonSupport(router))
        .serve
    } yield exitCode
  }
}
