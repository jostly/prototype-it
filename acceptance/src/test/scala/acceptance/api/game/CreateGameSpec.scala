package acceptance.api.player

import fixture.AbstractAcceptanceTest
import spray.http.StatusCodes

class CreateGameSpec extends AbstractAcceptanceTest {

  feature("Create a game") {
    scenario("successfully creating a game") {
      Given("game definition data")
      val data = Map(
        "name" -> "game001",
        "players" -> 3
      )

      When("the definition is posted to creation api")
      val reply = post(playCommandUrl("game"), data)

      Then("the server responds with 'Accepted'")
      reply.status should be (StatusCodes.Accepted)

      When("new events are polled")
      val events = pollEvents()

      Then("the events should contain a 'GameCreated' event")
      val gameCreatedEvents = events.collect { case ("GameCreated", d) => d }
      gameCreatedEvents should have length 1
    }
  }

}
