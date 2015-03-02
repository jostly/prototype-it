package prototypeapp.playcontext.command

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import command.Command

class CommandHandler extends Actor with LazyLogging {

  context.system.eventStream.subscribe(self, classOf[Command])

  def receive = {
    case x: AnyRef => logger.info(s"CommandHandler received $x")
  }

}
