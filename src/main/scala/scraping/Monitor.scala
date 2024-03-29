package scraping

import events.{Event, TicketsAvailableObserver}
import java.awt.Desktop
import java.net.URI

import scala.collection.concurrent.TrieMap
import javax.sound.sampled.AudioSystem

import scala.collection.mutable.ListBuffer

/**
  * Class monitoring multiple events to check if new tickets become available
  * @param scraper Sraper object, supplying the Monitor with possibility to check if tickets became available
  */
class Monitor(private val scraper: LiveNationScraper) {
  private val eventsMonitored = TrieMap[Event, Thread]()
  private val observers = ListBuffer.empty[TicketsAvailableObserver]

  /**
    * Starts new thread to monitor event - check if new tickets are available every 5 seconds.
    * If tickets become available, event website is opened and sound notification is played
    * @param event Event to monitor
    * @return true if monitoring started successfully, false if event is already monitored or has tickets available
    */
  def startMonitoring(event: Event): Boolean = {
    if (eventsMonitored.contains(event) || event.ticketsAvailable) return false
    val thread = new Thread {
      override def run(): Unit = {
        while (!scraper.ticketsAvailable(event)) {
          println(event.name + ": Still no tickets... ")
          try{
            Thread.sleep(5000)
          }catch{
            case _: InterruptedException => {
              Thread.currentThread().interrupt()
              return
            }
          }
        }
        sendNotification(event)
        //eventsMonitored.remove(event)
        stopMonitoring(event)
      }
    }
    eventsMonitored.put(event, thread)
    thread.start()
    true
  }

  def stopMonitoring(event: Event): Unit = eventsMonitored.remove(event) match {
    case Some(thread: Thread) => thread.interrupt()
    case None => ;
  }

  def addObserver(observer: TicketsAvailableObserver): Unit = observers += observer

  private def sendNotification(event: Event): Unit = {
    println("Tickets for " + event.name + " available!!!")
    playSound()
    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(new URI(event.getUrl))
    }
    observers.foreach(o => o.newTicketsAvailable(event))
  }


  private def playSound(): Unit = {
    val thread = new Thread() {
      override def run(): Unit = {
        val audioStream = AudioSystem.getAudioInputStream(new java.io.File("data/sounds/AirHorn.wav"))
        val clip = AudioSystem.getClip
        clip.open(audioStream)
        clip.loop(2)
        Thread.sleep(7000)
      }
    }
    thread.start()
  }

  def monitoredEvents(): List[Event] = eventsMonitored.keys.toList

}
