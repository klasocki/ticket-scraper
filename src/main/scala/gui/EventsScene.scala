package gui

import java.net.URI
import java.awt.Desktop

import scalafx.Includes._
import events.{Event, TicketsAvailableObserver}
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextField}
import scalafx.stage.Stage
import scraping.{LiveNationScraper, Monitor}

class EventsScene(private val sceneWidth: Double, private val sceneHeight: Double,
                  private val stage: Stage,
                  private val scraper: LiveNationScraper,
                  private val monitor: Monitor)
  extends Scene(sceneWidth, sceneHeight) with TicketsAvailableObserver {
  stylesheets += getClass.getResource("raven.css").toExternalForm

  private val offYTopLabel = 20
  private val offYSearch = 50
  private val offYList = 80
  private val offYDown = 560
  private val textFieldWidth = 200
  private val offX = 20
  private val buttonWidth = 150
  private val listWidth = 560

  var searchButton: EventsButton = _
  var resetButton: EventsButton = _
  var observeButton: EventsButton = _
  var getLinkButton: EventsButton = _
  var stopMonitorButton: EventsButton = _

  prepareButtons()
  prepareLabels()
  addButtonsHandler()
  monitor.addObserver(this)

  val textField = new TextField
  textField.layoutX = offX
  textField.layoutY = offYSearch
  textField.promptText = "concert/star/band name"
  textField.prefWidth = textFieldWidth

  val list = new EventsList(scraper.getEventList, offX, offYList)

  val monitoredEvents = new EventsList(List[Event](), 2 * offX + listWidth, offYList)

  content.addAll(monitoredEvents, textField, list)


  override def newTicketsAvailable(event: Event): Unit = monitoredEvents.remove(event)

  def prepareButtons(): Unit = {
    searchButton = new EventsButton("Search", offX + textFieldWidth, offYSearch)
    resetButton = new EventsButton("Show all", 2 * offX + textFieldWidth + buttonWidth, offYSearch)
    observeButton = new EventsButton("Monitor concert", offX, offYDown)
    getLinkButton = new EventsButton("Go to Web Page", 2 * offX + buttonWidth, offYDown)
    stopMonitorButton = new EventsButton("Stop monitoring", 2 * offX + listWidth, offYDown)

    getLinkButton.styleClass += "button-weblink"
    content.addAll(searchButton, resetButton, observeButton, getLinkButton, stopMonitorButton)
  }

  def addButtonsHandler(): Unit = {

    searchButton.onAction = handle {
      list.items = ObservableBuffer(scraper.searchForEvents(textField.getText()))
    }

    resetButton.onAction = handle {
      list.items = ObservableBuffer(scraper.getEventList)
    }

    observeButton.onAction = handle {
      val selectedEvent = list.selectionModel.apply.getSelectedItem
      if (selectedEvent == null) {
        new EventsAlert("You must select an event",
        "Event not selected", stage).showAndWait()
      } else if (!monitor.startMonitoring(selectedEvent)) {
        new EventsAlert("Already monitoring or has tickets",
        "Event already monitored or has tickets available", stage).showAndWait()
      } else {
        println("Monitoring  event started: " + selectedEvent.name)
        monitoredEvents.add(selectedEvent)
      }
    }

    getLinkButton.onAction = handle {
      val selectedEvent = list.selectionModel.apply.getSelectedItem
      if (selectedEvent == null) {
        new EventsAlert("You must select an event",
        "Event not selected", stage).showAndWait()
      } else {
        println("Event's URL: " + selectedEvent.getUrl)
        if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE))
        Desktop.getDesktop.browse(new URI(selectedEvent.getUrl))
      }
    }

    stopMonitorButton.onAction = handle {
      val selectedEvent = monitoredEvents.getSelected
      if (selectedEvent == null) {
        println("Nothing selected!")
        new EventsAlert("You must select an event",
        "Event not selected", stage).showAndWait()
      } else {
        monitor.stopMonitoring(selectedEvent)
        println("Stopping monitoring event:" + selectedEvent.name)
        monitoredEvents.remove(selectedEvent)
      }
    }
  }

  def prepareLabels(): Unit = {
    val searchLabel = new Label("Search for concert:")
    searchLabel.layoutX = offX
    searchLabel.layoutY = offYTopLabel

    val monitorLabel = new Label("Monitored events:")
    monitorLabel.layoutX = 2 * offX + listWidth
    monitorLabel.layoutY = offYSearch

    content.addAll(searchLabel, monitorLabel)

  }

}
