package prototypeapp.playcontext.resource

import infrastructure.Resource
import prototypeapp.playcontext.command.{DrawCardCommand, JoinGameCommand}
import prototypeapp.playcontext.resource.CommandRoute._
import spray.http.StatusCodes

object CommandRoute {
  case class Draw(player: String, from: String, to: Option[String])
  case class JoinGame(playerName: String, gameId: String)

  case class GameJoined(token: String)
}

trait CommandRoute { self: Resource =>

  val commandRoute =
    pathPrefix("draw") {
      post {
        entity(as[Draw]) { draw =>
          logger.info(s"Received '$draw'")
          publish(DrawCardCommand(draw.player, draw.from, draw.to))
          complete(StatusCodes.Accepted)
        }
      }
    } ~
    pathPrefix("player") {
      pathPrefix("join") {
        post {
          entity(as[JoinGame]) { request =>
            logger.info(s"Received $request")
            publish(JoinGameCommand(request.playerName, request.gameId))
            complete(StatusCodes.Accepted -> GameJoined("blabla"))
          }
        }
      }
    }
}