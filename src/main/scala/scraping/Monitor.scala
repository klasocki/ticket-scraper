package scraping

import events.Event
import java.awt.Desktop
import java.net.URI
import scala.collection.concurrent.TrieMap

import javax.sound.sampled.AudioSystem

class Monitor(private val scraper: LiveNationScraper) {
  private val eventsMonitored = TrieMap[Event, Thread]()

  def startMonitoring(event: Event): Unit = {
    val thread = new Thread {
      override def run(): Unit = {
        while (!scraper.ticketsAvailable(event)) {
          println(event.name + ": Still no tickets... ")
          Thread.sleep(5000)
        }
        sendNotification(event)
        eventsMonitored.remove(event)
      }
    }
    eventsMonitored.put(event, thread)
    thread.start()
  }

  def stopMonitoring(event: Event): Unit = eventsMonitored.remove(event) match {
    case Some(thread: Thread) => thread.interrupt()
    case None => ;
  }

  private def sendNotification(event: Event): Unit = {
    println("Tickets for " + event.name + " available!!!")
    playSound()
    val keyword = event.name.replaceAll("\\s+", "%20")
    val page = "https://www.livenation.pl/search?keyword=" + keyword
    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(new URI(page))
    }
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