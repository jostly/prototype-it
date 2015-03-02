package fixture

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}
import org.scalatest._
import prototypeapp.playcontext.application.PlayApplication
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.Json4sSupport
import spray.httpx.unmarshalling._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

abstract class AbstractAcceptanceTest extends TestKit(ActorSystem("acceptance"))
with UUIDGenerator
with ImplicitSender with Json4sSupport
with FeatureSpecLike with Matchers with GivenWhenThen
with BeforeAndAfterAll with BeforeAndAfterEach {

  implicit val executionContext = system.dispatcher

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  override def afterAll {
    playApplication.close()
    TestKit.shutdownActorSystem(system)
  }

  override def beforeAll {
    playApplication.start()
  }

  val playApplication = new PlayApplication(system, playApplicationPort)

  def playApplicationPort: Int = 8080

  def post(url: String, payload: AnyRef, timeout: Duration = 1.second): HttpResponse = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post(url, payload)), timeout)
  }

  def postWithResponse[T: FromResponseUnmarshaller](url: String, payload: AnyRef, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val result = Await.result(pipeline(Post(url, payload)), timeout)
    val un = unmarshal[T]
    (result, un(result))
  }

  def get[T](url: String, t: (HttpResponse) => T, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[T] = sendReceive ~> t
    Await.result(pipeline(Get(url)), timeout)
  }

  def getJson(url: String, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val s = Await.result(pipeline(Get(url)), timeout).entity.asString
    parse(s)
  }

  def host: String = "http://localhost"

  def playCommandUrl: String = s"$host:$playApplicationPort/service"

  def playCommandUrl(s: String): String = s"$playCommandUrl/$s"
  

}

