package events

class Event(val name: String, val venue: String, val date: String, val tickets: String, val url: String = "https://www.livenation.pl/") {
  def ticketsAvailable(): Boolean = {
    val t = tickets.trim()
    t.matches("(?i)Znajd[źz]\\s+bilety") || t.matches("(?i)Zobacz\\s+wi[ęe]cej")
  }

  override def toString: String = {
    "[Name]: " + name + " [Venue]: " + venue + " [Date]: " + date + " [Tickets?]: " + ticketsAvailable
  }

  def getUrl(): String = {
    url match {
      case "https://www.livenation.pl/" => url
      case _ => "https://www.livenation.pl" + url
    }
  }
}
