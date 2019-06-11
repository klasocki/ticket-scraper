package scraping

import events.Event
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

import scala.annotation.tailrec


class LiveNationScraper(val rootURL: String) {
  private val browser = JsoupBrowser()

  def searchForEvents(name: String): List[Event] = {
    null
  }

  def ticketsAvailable(event: Event): Boolean = {
    false
  }

  def getEventList: List[Event] = {
    @tailrec
    def getEventsRec(pageNumber: Int, acc: List[Event]): List[Event] = {
      val list = browser.get(rootURL + "/event/allevents?page=" + pageNumber) >>
        element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")
      val events = list.map(event => new Event(
        event >> allText(".result-info__localizedname"),
        event >> allText(".result-info__venue"),
        event >> allText(".event-date__date"),
        event >> allText(".result-card__actions"))
      )

      if (list.isEmpty) acc
      else getEventsRec(pageNumber + 1, acc ++ events)
    }

    val acc = List.empty[Event]
    getEventsRec(1, acc)
  }
}
