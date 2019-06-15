import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, Button, Label, ListView, TextField}
import scraping.{LiveNationScraper, Monitor, SearchEngine}
import scalafx.Includes._
import java.awt.Desktop
import java.net.URI
import events.Event
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Alert.AlertType

object Main extends JFXApp {

  private val offX = 20
  private val offYTopLabel = 20
  private val offYSearch = 50
  private val offYList = 80
  private val offYDown = 560
  private val textFieldWidth = 200
  private val listWidth = 560
  private val listHeight = 480
  private val sceneSize = 600
  private val buttonWidth = 150
  private val scraper = new LiveNationScraper("https://www.livenation.pl/event/allevents?page=")
  private val monitor = new Monitor(scraper)

  stage = new application.JFXApp.PrimaryStage {
    title = "Events monitor"
    scene = new Scene(sceneSize * 2 - offX, sceneSize) {

      val label = new Label("Search for concert:")
      label.layoutX = offX
      label.layoutY = offYTopLabel

      val textField = new TextField
      textField.layoutX = offX
      textField.layoutY = offYSearch
      textField.promptText = "concert/star/band name"
      textField.prefWidth = textFieldWidth

      val button = new Button("Search")
      button.layoutX = offX + textFieldWidth
      button.layoutY = offYSearch

      val list = new ListView(scraper.getEventList)

      list.layoutX = offX
      list.layoutY = offYList
      list.prefWidth = listWidth
      list.prefHeight = listHeight

      val monitorLabel = new Label("Monitored events:")
      monitorLabel.layoutX = 2 * offX + listWidth
      monitorLabel.layoutY = offYSearch

      val monitorList = new ListView(List[Event]())
      monitorList.layoutX = 2 * offX + listWidth
      monitorList.layoutY = offYList
      monitorList.prefWidth = listWidth
      monitorList.prefHeight = listHeight

      val observeButton = new Button("Monitor concert")
      observeButton.layoutX = offX
      observeButton.layoutY = offYDown
      observeButton.prefWidth = buttonWidth


      val getLinkButton = new Button("Go to Web Page")
      getLinkButton.layoutX = 2 * offX + buttonWidth
      getLinkButton.layoutY = offYDown
      getLinkButton.prefWidth = buttonWidth

      val stopMonitorButton = new Button("Stop monitoring")
      stopMonitorButton.layoutX = 2 * offX + listWidth
      stopMonitorButton.layoutY = offYDown
      stopMonitorButton.prefWidth = buttonWidth

      content = List(monitorLabel, monitorList, label, textField, button, list, observeButton, getLinkButton, stopMonitorButton)

      button.onAction = handle {
        list.items = ObservableBuffer(SearchEngine.searchForEvents(scraper.getEventList, textField.getText()))
      }

      observeButton.onAction = handle {
        val selectedEvent = list.selectionModel.apply.getSelectedItem
        if (selectedEvent == null) {
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Not selected!"
            headerText = "Error!"
            contentText = "You must select event to monitor from events list!"
          }.showAndWait()
        } else if (selectedEvent.ticketsAvailable()) {
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Cannot monitor!"
            headerText = "Error!"
            contentText = "Cannot monitor Event which has avalaible tickets !"
          }.showAndWait()
        } else {
          println("Monitoring  event: " + selectedEvent.name)
          monitor.startMonitoring(selectedEvent)
          monitorList.items.getValue.add(selectedEvent)
        }
      }

      getLinkButton.onAction = handle {
        val selectedEvent = list.selectionModel.apply.getSelectedItem
        if (selectedEvent == null) {
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Not selected!"
            headerText = "Error!"
            contentText = "You must select event to get its link from events list!"
          }.showAndWait()
        } else {
          println("Event's URL: " + selectedEvent.getUrl())
          if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE))
            Desktop.getDesktop.browse(new URI(list.selectionModel.apply.getSelectedItem.getUrl()))
        }


      }
      stopMonitorButton.onAction = handle {
        val selectedEvent = monitorList.selectionModel.apply.getSelectedItem
        if (selectedEvent == null) {
          println("Nothing selected!")
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Not selected!"
            headerText = "Error!"
            contentText = "You must select event to stop from monitored events list!"
          }.showAndWait()
        } else {
          monitor.stopMonitoring(selectedEvent)
          println("Stopping monitoring event:" + selectedEvent.name)
          monitorList.items.getValue.remove(selectedEvent)

        }
      }

    }
  }
}
