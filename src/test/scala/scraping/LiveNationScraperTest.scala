package scraping

import events.Event
import org.scalatest.{BeforeAndAfter, FunSuite}

class LiveNationScraperTest extends FunSuite with BeforeAndAfter{
  var scraper: LiveNationScraper = _
  before{
    scraper = new LiveNationScraper("src/test/mocks/allevents/no-tickets-for-james")
  }

  test("shouldFindDisturbedWithTypo") {
    val events = scraper.searchForEvents("disturbd")
    assert(events.exists((e: Event) => e.name == "Disturbed" && e.venue == "Torwar"))
  }

  test("shouldFindRodByPrefix") {
    val events = scraper.searchForEvents("Rod Stewart")
    assert(events.exists((e: Event) => e.name == "Rod Stewart: Live in Concert" && e.venue == "TAURON Arena Kraków"))
  }

  test("shouldNotFindKult") {
    val events = scraper.searchForEvents("Kult")
    assert(events.isEmpty)
  }

  test("shouldFindBothMarkShows") {
    val events = scraper.searchForEvents("mark knopfler")
    assert(events.length == 2)
  }

  test("testTicketsNotAvailableForJamesWithTypo") {
    val events = scraper.searchForEvents(" jamers b")
    assert(!scraper.ticketsAvailable(events.head))
  }

  test("testTicketsAvailableForKissWithoutFullConcertName") {
    val events = scraper.searchForEvents("kiss")
    assert(scraper.ticketsAvailable(events.head))
  }

  test("testEventListLength") {
    val events = scraper.getEventList
    assert(events.length == 20)
  }
}
