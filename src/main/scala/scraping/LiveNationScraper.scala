package scraping

import events.Event

class LiveNationScraper {
  def searchForEvents(name: String): List[Event] = {
    null
  }

  def ticketsAvailable(event: Event): Boolean = {
    false
  }
  
  def scrappedList(pageNumber: String): List[(String,String)]={

    val browser = JsoupBrowser()

    val doc = browser.get("https://www.livenation.pl/event/allevents?page=" + pageNumber )

    var tmp = doc >> element(".allevents__eventlist")

    var list = tmp >> elementList("li")
    var listNames = list.map(_ >> (allText(".result-info__headliners"), allText(".result-card__actions")))
    for (elem <- listNames) {
      println("Tytul: " + elem)
    }
    
    return listNames

  }
}
