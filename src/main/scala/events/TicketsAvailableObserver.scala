package events

trait TicketsAvailableObserver {
   def newTicketsAvailable(event: Event)
}
