package scraping

import java.io.FileNotFoundException

import events.Event
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

import scala.annotation.tailrec


class LiveNationScraper(val rootURL: String) {
  private val browser = JsoupBrowser()

  def searchForEvents(name: String): List[Event] = SearchEngine.searchForEvents(getEventList, name)

  def ticketsAvailable(event: Event): Boolean =
    getEventList.find(e => e.name == event.name) match {
      case Some(ev) => ev.ticketsAvailable()
      case None => false
    }

  private def getElementsFromFileOrURL(string: String): List[Element] = {
    if (string.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
      browser.get(string) >> element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")
    }
    else {
      try {
        browser.parseFile(string + ".html") >> element(".allevents__eventlist") >>
          elementList(".allevents__eventlistitem")
      } catch {
        case _: FileNotFoundException =>
          List[Element]()
      }
    }

  }

  def getEventList: List[Event] = {
    @tailrec
    def getEventsRec(pageNumber: Int, acc: List[Event]): List[Event] = {

      val list = getElementsFromFileOrURL(rootURL + pageNumber)


      val events = list.map(event => new Event(
        event >> allText(".result-info__localizedname"),
        event >> allText(".result-info__venue"),
        event >> allText(".event-date__date"),
        event >> allText(".result-card__actions"),
        (event >> element("a")).attr("href"))

      )

      if (list.isEmpty) acc
      else getEventsRec(pageNumber + 1, acc ++ events)
    }

    val acc = List.empty[Event]
    getEventsRec(1, acc)
  }
}
