package events

class Event(val name: String, val venue: String, val date: String, val tickets: String) {
  def ticketsAvailable(): Boolean = {
    val t = tickets.trim()
    t.matches("(?i)Znajd[źz]\\s+bilety") || t.matches("(?i)Zobacz\\s+wi[ęe]cej")
  }
}
