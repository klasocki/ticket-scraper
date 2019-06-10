package scraping

import events.Event
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._


class LiveNationScraper(val rootURL: String) {
  private val browser = JsoupBrowser()

  def searchForEvents(name: String): List[Event] = {
    null
  }

  def ticketsAvailable(event: Event): Boolean = {
    false
  }

  def getEventList(pageNumber: Int = 1): List[Event] = {
    val doc = browser.get(rootURL + "/event/allevents?page=" + pageNumber)

    val tmp = doc >> element(".allevents__eventlist")
    val list = tmp >> elementList(".allevents__eventlistitem")

    val outpList = scala.collection.mutable.ListBuffer.empty[Event]

    for (event <- list) {
      outpList += new Event(event >> allText(".result-info__localizedname"), event >> allText(".result-info__venue")
        , event >> allText(".event-date__date"), event >> allText(".result-card__actions"))
    }

    if (list.isEmpty) outpList.toList
    else outpList.toList ++ getEventList(pageNumber + 1)
  }
}
