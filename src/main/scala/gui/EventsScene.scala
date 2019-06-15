package gui

import java.net.URI
import java.awt.Desktop

import scalafx.Includes._
import events.Event
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{ Label, TextField}
import scalafx.stage.Stage
import scraping.{LiveNationScraper, Monitor, SearchEngine}

class EventsScene(private val sceneWidth: Double, private val sceneHeight: Double, private val stage: Stage
                  , private val scraper: LiveNationScraper, private val monitor: Monitor) extends Scene(sceneWidth, sceneHeight) {

  private val offYTopLabel = 20
  private val offYSearch = 50
  private val offYList = 80
  private val offYDown = 560
  private val textFieldWidth = 200
  private val offX = 20
  private val buttonWidth = 150
  private val listWidth = 560

  val label = new Label("Search for concert:")
  label.layoutX = offX
  label.layoutY = offYTopLabel

  val textField = new TextField
  textField.layoutX = offX
  textField.layoutY = offYSearch
  textField.promptText = "concert/star/band name"
  textField.prefWidth = textFieldWidth

  val searchButton = new EventsButton("Search", offX + textFieldWidth, offYSearch)

  val resetButton = new EventsButton("Show all", 2 * offX + textFieldWidth + buttonWidth, offYSearch)

  val list = new EventsList(scraper.getEventList, offX, offYList)

  val observeButton = new EventsButton("Monitor concert", offX, offYDown)

  val getLinkButton = new EventsButton("Go to Web Page", 2 * offX + buttonWidth, offYDown)

  val monitorLabel = new Label("Monitored events:")
  monitorLabel.layoutX = 2 * offX + listWidth
  monitorLabel.layoutY = offYSearch

  val monitorList = new EventsList(List[Event](), 2 * offX + listWidth, offYList)

  val stopMonitorButton = new EventsButton("Stop monitoring", 2 * offX + listWidth, offYDown)

  content = List(monitorLabel, monitorList, label, textField, searchButton, list,
    observeButton, getLinkButton, stopMonitorButton, resetButton)

  searchButton.onAction = handle {
    list.items = ObservableBuffer(SearchEngine.searchForEvents(scraper.getEventList, textField.getText()))
  }

  resetButton.onAction = handle {
    list.items = ObservableBuffer(scraper.getEventList)
  }

  observeButton.onAction = handle {
    val selectedEvent = list.selectionModel.apply.getSelectedItem
    if (selectedEvent == null) {
      new EventsAlert(AlertType.Error, "You must select event to monitor!", "Not selected", stage).showAndWait()
    } else if (selectedEvent.ticketsAvailable()) {
      new EventsAlert(AlertType.Error, "Cannot monitor event which has avalaible tickets!", "Cannot monitor", stage).showAndWait()
    } else if (!monitor.startMonitoring(selectedEvent)) {
      new EventsAlert(AlertType.Error, "This event is already being monitored!", "Already monitoring!", stage).showAndWait()
    } else {
      println("Monitoring  event: " + selectedEvent.name)
      monitorList.items.getValue.add(selectedEvent)
    }
  }

  getLinkButton.onAction = handle {
    val selectedEvent = list.selectionModel.apply.getSelectedItem
    if (selectedEvent == null) {
      new EventsAlert(AlertType.Error, "You must select event to get its link from events list!", "Not selected", stage).showAndWait()
    } else {
      println("Event's URL: " + selectedEvent.getUrl)
      if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE))
        Desktop.getDesktop.browse(new URI(selectedEvent.getUrl))
    }


  }
  stopMonitorButton.onAction = handle {
    val selectedEvent = monitorList.selectionModel.apply.getSelectedItem
    if (selectedEvent == null) {
      println("Nothing selected!")
      new EventsAlert(AlertType.Error, "You must select event to stop from monitored events list!", "Not selected!", stage).showAndWait()
    } else {
      monitor.stopMonitoring(selectedEvent)
      println("Stopping monitoring event:" + selectedEvent.name)
      monitorList.items.getValue.remove(selectedEvent)

    }
  }

}
