package prototypeapp.playcontext.command

import command.Command

case class DrawCardCommand(player: String, from: String, to: Option[String])
  extends Command
