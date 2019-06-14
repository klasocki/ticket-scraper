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

  def searchForEvents(name: String): List[Event] = {
    null
  }

  def ticketsAvailable(event: Event): Boolean = {
    false
  }

  def getElementList(string: String): List[Element] = {

    if (string.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
      val doc = browser.get(string) >> element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")
      return doc
    }
    else {
      try {
        val doc = browser.parseFile(string + ".html") >> element(".allevents__eventlist") >>
          elementList(".allevents__eventlistitem")
        return doc
      } catch {
        case noFile: FileNotFoundException => List[Element]()
      }

    }

  }

  def getEventList: List[Event] = {
    @tailrec
    def getEventsRec(pageNumber: Int, acc: List[Event]): List[Event] = {

      val list = getElementList(rootURL + pageNumber)

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
