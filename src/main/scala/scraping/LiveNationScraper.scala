package scraping

import java.io.FileNotFoundException

import events.Event
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

import scala.annotation.tailrec


class LiveNationScraper(val rootURL: String = "https://www.livenation.pl/event/allevents?page=") {
  private val browser = JsoupBrowser()


  def searchForEvents(name: String): List[Event] = SearchEngine.searchForEvents(getEventList, name)

  def ticketsAvailable(event: Event): Boolean =
    getEventList.find(e => e.name == event.name) match {
      case Some(ev) => ev.ticketsAvailable()
      case None => false
    }


  def getEventList: List[Event] = {

    var list: List[Element] = List[Element]()
    if (rootURL.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
      list = getElementsFromUrl()
    } else {
      list = getElementsFromFile()
    }

    list.map(event => new Event(
      event >> allText(".result-info__localizedname"),
      event >> allText(".result-info__venue"),
      event >> allText(".event-date__date"),
      event >> allText(".result-card__actions"),
      (event >> element("a")).attr("href"))
    )

  }

  private def getElementsFromUrl(): List[Element] = {

    @tailrec
    def getEventsRec(pageNumber: Int, acc: List[Element]): List[Element] = {

      val list = browser.get(rootURL + pageNumber) >> element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")

      if (list.isEmpty) acc
      else getEventsRec(pageNumber + 1, acc ++ list)
    }

    val acc = List.empty[Element]
    getEventsRec(1, acc)
  }

  private def getElementsFromFile(): List[Element] = {

    try {
      browser.parseFile(rootURL + ".html") >> element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")
    } catch {
      case _: FileNotFoundException =>
        List[Element]()
    }
  }

}
