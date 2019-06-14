import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ListView, TextField}
import scraping.LiveNationScraper
import scraping.SearchEngine
import events.Event
import javafx.collections.FXCollections
import scalafx.Includes._
import java.awt.Desktop
import java.net.URI

import scalafx.collections.ObservableBuffer

import scala.collection.JavaConverters._

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

  stage = new application.JFXApp.PrimaryStage {
    title = "Events monitor"
    scene = new Scene(sceneSize, sceneSize) {
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

      var list = new ListView(scraper.getEventList)
      list.layoutX = offX
      list.layoutY = offYList
      list.prefWidth = listWidth
      list.prefHeight = listHeight

      val observeButton = new Button("Monitor concert")
      observeButton.layoutX = offX
      observeButton.layoutY = offYDown
      observeButton.prefWidth = buttonWidth

      val getLinkButton = new Button("Go to Web Page")
      getLinkButton.layoutX = 2 * offX + buttonWidth
      getLinkButton.layoutY = offYDown
      getLinkButton.prefWidth = buttonWidth

      content = List(label, textField, button, list, observeButton, getLinkButton)

      button.onAction = handle {
        val found = SearchEngine.searchForEvents(scraper.getEventList, textField.getText())
        list.items= ObservableBuffer (found)
      }

      observeButton.onAction = handle {
        println("Monitor event: ")
        println(list.selectionModel.apply.getSelectedItem)
      }
      getLinkButton.onAction = handle {
        println("Event's URL: ")
        println(list.selectionModel.apply.getSelectedItem.getUrl())

        if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE))
          Desktop.getDesktop.browse(new URI(list.selectionModel.apply.getSelectedItem.getUrl()))

      }
    }
  }
}
