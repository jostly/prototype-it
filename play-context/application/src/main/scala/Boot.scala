import akka.actor.ActorSystem
import prototypeapp.playcontext.application.PlayApplication

object Boot extends App {

  new PlayApplication(ActorSystem("order-context"), port = 8080).start()

}
