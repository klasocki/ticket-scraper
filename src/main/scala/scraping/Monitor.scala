package scraping

import events.Event
import java.awt.Desktop
import java.net.URI

import javax.sound.sampled.AudioSystem

import scala.collection.mutable.ListBuffer

class Monitor(private val scraper: LiveNationScraper) {
  private val list = ListBuffer.empty[Event]

  def startMonitoring(event: Event): Unit = {
    list.synchronized {
      list += event
    }
    val thread = new Thread {
      override def run(): Unit = {
        while (!scraper.ticketsAvailable(event)) {
          println(event.name + ": Still no tickets... ")
          Thread.sleep(5000)
        }
        sendNotification(event)
        list.synchronized {
          list -= event
        }
      }
    }
    thread.start()
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
    val thread = new Thread(){
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

  def monitoredEvents(): List[Event] = list.toList

}
