package events

/**
  * Class representing an event
  * @param name Official event name
  * @param venue Venue of the event
  * @param date Event's date
  * @param tickets String scraped from html with ticket information in polish
  * @param url Event's url
  */
class Event(val name: String, val venue: String, val date: String, val tickets: String, val url: String = "https://www.livenation.pl/") {
  /**
    * Checks if event tickets are available - assumes that they are if event has either
    * "Znajdz bilety" (find tickets) or "Zobacz wiecej" (see more) as options
    * @return True if tickets are available
    */
  def ticketsAvailable(): Boolean = {
    val t = tickets.trim()
    t.matches("(?i)Znajd[źz]\\s+bilety") || t.matches("(?i)Zobacz\\s+wi[ęe]cej")
  }

  override def toString: String = {
    List(name, venue, date, if(ticketsAvailable()) "Tickets available" else "No tickets").reduce(_ + " | " + _)
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Event]

  override def equals(other: Any): Boolean = other match {
    case that: Event =>
      (that canEqual this) &&
        name == that.name &&
        venue == that.venue &&
        date == that.date &&
        tickets == that.tickets &&
        url == that.url
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, venue, date, tickets, url)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  def getUrl: String = url match {
    case "https://www.livenation.pl/" => url
    case _ => "https://www.livenation.pl" + url
  }
}
