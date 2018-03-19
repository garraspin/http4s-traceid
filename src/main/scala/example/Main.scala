package example

import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, TimeUnit}

import Main.logger
import cats.effect.IO
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.executors.util.ContextAwareExecutorService
import kamon.http4s.middleware._
import org.http4s.client.blaze.Http1Client
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.{Headers, HttpService, Request, Uri}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

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

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    ContextAwareExecutorService(ExecutionContextExecutorServiceBridge(ExecutionContext.global))
  )

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    for {
      http4sclient <- Http1Client.stream[IO]()
      client        = kamon.http4s.middleware.client.KamonSupport(http4sclient)
      router  = Router(
        "/" -> HttpService[IO] {
          case req@GET -> Root / "foo" =>
            for {
              _   <- IO { 42 }
              t1   = Thread.currentThread().getName
              res <- client.expect[String](Request[IO](method = GET, uri = Uri.unsafeFromString(s"http://localhost:$port/bar"), headers = Headers.empty))
              t2   = Thread.currentThread().getName
              _    = if (t1 != t2) logger.info(s"Sometimes no TraceID when jumping from $t1 to $t2")
              r   <- Ok(s"GET foo and $res")
            } yield r

          case req@GET -> Root / "bar" =>
            logger.info("bar hs: " + req.headers.map(h => h.name -> h.value).mkString(", "))
            Ok("bar")
        }
      )

      exitCode <- BlazeBuilder[IO]
        .bindHttp(port, "0.0.0.0")
        .mountService(server.KamonSupport(router))
        .serve
    } yield exitCode
  }
}
