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

  def getEventList(pageNumber: Int = 1): List[(String, String)] = {
    val doc = browser.get(rootURL + "/event/allevents?page=" + pageNumber)
    val tmp = doc >> element(".allevents__eventlist")
    val list = tmp >> elementList("li")
    val listNames = list.map(_ >> (allText(".result-info__headliners"), allText(".result-card__actions")))
    if(listNames.isEmpty) listNames
    else listNames ++ getEventList(pageNumber + 1)
  }
}
