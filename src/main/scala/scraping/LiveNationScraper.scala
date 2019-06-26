package scraping

import events.Event
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

import scala.annotation.tailrec

/**
  * Provides event list, as well as checks for ticket availability and access to event search engine
  * @param resource Either a url to website with events list, or path to html file with saved website for testing
  */
class LiveNationScraper(val resource: String = "https://www.livenation.pl/event/allevents?page=") {
  private val browser = JsoupBrowser()

  /**
    * Search for events using our custom search engine
    * @see [[scraping.SearchEngine]]
    * @param name Name of event to search
    * @return List of events matching the given name
    */
  def searchForEvents(name: String): List[Event] = SearchEngine.searchForEvents(getEventList, name)

  /**
    * Check if event currently has tickets available
    * @param event Event to check
    * @return True if tickets available
    */
  def ticketsAvailable(event: Event): Boolean =
    getEventList.find(e => e.name == event.name) match {
      case Some(ev) => ev.ticketsAvailable()
      case None => false
    }


  /**
    * Get list of current events, either from website or html file
    * @return List of events
    * @see [[events.Event]]
    */
  def getEventList: List[Event] = {

    var list: List[Element] = List[Element]()
    if (resource.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
      list = getElementsFromUrl
    } else {
      list = getElementsFromFile
    }

    list.map(event => new Event(
      event >> allText(".result-info__localizedname"),
      event >> allText(".result-info__venue"),
      event >> allText(".event-date__date"),
      event >> allText(".result-card__actions"),
      (event >> element("a")).attr("href"))
    )

  }

  private def getElementsFromUrl: List[Element] = {

    @tailrec
    def getEventsRec(pageNumber: Int, acc: List[Element]): List[Element] = {

      val list = browser.get(resource + pageNumber) >> element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")

      if (list.isEmpty) acc
      else getEventsRec(pageNumber + 1, acc ++ list)
    }

    val acc = List.empty[Element]
    getEventsRec(1, acc)
  }

  private def getElementsFromFile: List[Element] =
     browser.parseFile(resource) >> element(".allevents__eventlist") >>
        elementList(".allevents__eventlistitem")

}
