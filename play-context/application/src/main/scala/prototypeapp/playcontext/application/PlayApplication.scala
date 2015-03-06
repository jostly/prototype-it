package prototypeapp.playcontext.application

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import infrastructure.{BindActor, Resource}
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}
import prototypeapp.playcontext.command.CommandHandler
import prototypeapp.playcontext.resource.{CommandRoute, QueryRoute}
import spray.can.Http

import scala.concurrent.Await
import scala.concurrent.duration._

class PlayApplication(system: ActorSystem, port: Int) {

  system.actorOf(Props(classOf[CommandHandler]), "command-handler")

  val router = system.actorOf(Props(classOf[PlayRoutingActor]), "play-routing")
  val binder = system.actorOf(Props(classOf[BindActor]), "play-binding")

  implicit val timeout = Timeout(5.seconds)

  def start() = {
    Await.result(binder ? Http.Bind(router, interface = "localhost", port = port), 5.seconds)
  }

  def close(): Unit = {
    Await.result(binder ? Http.Unbind, 5.seconds)
  }

}

class PlayRoutingActor
  extends Actor with CommandRoute with QueryRoute with Resource {

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def actorRefFactory = context

  def receive = runRoute(
    pathPrefix("service") {
      commandRoute
    } ~
    pathPrefix("query") {
      queryRoute
    }
  )
}