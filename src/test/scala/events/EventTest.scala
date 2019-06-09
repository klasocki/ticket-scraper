package events

import org.scalatest.FunSuite

class EventTest extends FunSuite {

  test("testTicketsShouldBeAvailableWhenLowerCase") {
    assert(new Event("_", "_", "_", "znajdź bilety").ticketsAvailable())
  }
  test("testTicketsShouldBeAvailableWhenNotPolishChars") {
    assert(new Event("_", "_", "_", "Zobacz wiecej").ticketsAvailable())
  }
  test("testTicketsShouldNotBeAvailable") {
    assert(!new Event("_", "_", "_", "Brak").ticketsAvailable())
  }
}
