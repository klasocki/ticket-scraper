package events

/**
  * Observer interface, to make sure event is deleted from monitored list in gui after tickets become available
  */
trait TicketsAvailableObserver {
   def newTicketsAvailable(event: Event)
}
