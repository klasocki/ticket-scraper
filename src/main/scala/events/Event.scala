package events

class Event(val name: String, val venue: String, val date: String, val tickets: String, val url: String = "https://www.livenation.pl/") {
  def ticketsAvailable(): Boolean = {
    val t = tickets.trim()
    t.matches("(?i)Znajd[źz]\\s+bilety") || t.matches("(?i)Zobacz\\s+wi[ęe]cej")
  }

  override def toString: String = {
    "[Name]: " + name + " [Venue]: " + venue + " [Date]: " + date + " [Tickets?]: " + ticketsAvailable
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Event]

  override def equals(other: Any): Boolean = other match {
    case that: Event =>
      (that canEqual this) &&
        name == that.name &&
        venue == that.venue &&
        date == that.date &&
        tickets == that.tickets
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, venue, date, tickets)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)


  }
}
