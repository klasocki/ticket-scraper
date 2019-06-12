package scraping

import events.Event
import org.scalatest.FunSuite

class LiveNationScraperTest extends FunSuite {

  test("testSearchForEvents") {
    val scraper = new LiveNationScraper("src/test/docs/event/allevents")
    val events = scraper.searchForEvents("Hozier")
    assert(events.exists((e: Event) => e.name == "Hozier" && e.venue == "Palladium: Warsaw"))
  }
  test("testTicketsAvailable") {
    val scraper = new LiveNationScraper("src/test/docs/event/allevents")
    val events = scraper.searchForEvents("Hozier")
    assert(!scraper.ticketsAvailable(events.head))
  }

  test("testEventListLength") {
    val scraper = new LiveNationScraper("src/test/docs/event/allevents")
    val events = scraper.getEventList
    assert(events.length == 20)
  }
}
