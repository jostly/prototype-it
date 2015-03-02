package prototypeapp.playcontext.command

import command.Command

case class JoinGameCommand(playerName: String, gameId: String) extends Command
