package scraping

import events.Event

import java.awt.Desktop
import java.net.URI
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
    val keyword = event.name.replaceAll("\\s+", "%20")
    val page = "https://www.livenation.pl/search?keyword=" + keyword
    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(new URI(page))
    }
  }

  def monitoredEvents(): List[Event] = list.toList

}
