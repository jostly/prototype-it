package acceptance.api.player

import fixture.AbstractAcceptanceTest
import prototypeapp.playcontext.resource.CommandRoute
import spray.http.StatusCodes

class PlayerJoinGameSpec extends AbstractAcceptanceTest {

  feature("Player joins game") {
    scenario("A new player joins an existing game") {
      Given("an existing game")
      
      When("the player joins the game")
      val (reply, message) = postWithResponse[CommandRoute.GameJoined](
        playCommandUrl("player/join"),
        CommandRoute.JoinGame("playerId", "gameId"))

      Then("the status should be 'Accepted'")
      reply.status should be (StatusCodes.Accepted)

      And("the message should contain a token")
      message.token should not be 'empty

    }

  }

}
