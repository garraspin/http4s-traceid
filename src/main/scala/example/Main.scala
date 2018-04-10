package example

import java.util.concurrent._
import java.util.concurrent.atomic.AtomicLong

import cats.implicits._
import cats.effect.{Effect, IO}
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.executors.util.ContextAwareExecutorService
import kamon.http4s.middleware._
import org.http4s.client.Client
import org.http4s.client.blaze.{BlazeClientConfig, Http1Client}
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.{Headers, HttpService, Request, Uri}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

object Main extends StreamApp[IO] with Logging {

  val port = 9000

  def service(client: Client[IO])  =
    HttpService[IO] {
      case GET -> Root / "foo" =>
        for {
          _   ← IO { logger.info(s"1. /foo") }
          res ← client.expect[String](Request[IO](method = GET, uri = Uri.unsafeFromString(s"http://localhost:$port/bar"), headers = Headers.empty))
          _   ← IO { logger.info(s"3. /foo") }
          r   ← Ok(s"GET foo and $res\n")
        } yield r

      case GET -> Root / "bar" =>
        IO { logger.info("2. /bar") } *> Ok("bar")
    }

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    for {
      client   ← Http1Client
                  .stream[IO](BlazeClientConfig.defaultConfig.copy(executionContext = ioExecutionContext))
                  .map(kamon.http4s.middleware.client.KamonSupport[IO])
                  //.map(MyKamonSupport[IO]) // Using this version the context is properly propagated
      router   = Router("/" → service(client))
      exitCode ← BlazeBuilder[IO]
                  .bindHttp(port, "0.0.0.0")
                  .mountService(server.KamonSupport(router))
                  .withExecutionContext(cpuExecutionContext)
                  .serve(Effect[IO], cpuExecutionContext)
    } yield exitCode
  }

  val ioExecutionContext =
    ExecutionContext.fromExecutor(ContextAwareExecutorService(Executors.newCachedThreadPool(new ThreadFactory {
      private val counter = new AtomicLong(0L)

      def newThread(r: Runnable) = {
        val thread = new Thread(r)
        thread.setName("io-thread-" + counter.getAndIncrement.toString)
        thread.setDaemon(true)
        thread
      }
    })))

  val cpuExecutionContext =
    ExecutionContext.fromExecutorService(ContextAwareExecutorService(new ForkJoinPool()))

}

trait Logging {
  lazy val logger = LoggerFactory.getLogger(this.getClass)
}

