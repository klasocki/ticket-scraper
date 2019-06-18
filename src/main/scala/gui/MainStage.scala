package gui

import javafx.stage.WindowEvent
import scalafx.application
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType
import scraping.{LiveNationScraper, Monitor}

class MainStage(private val stageTitle: String, private val scraper: LiveNationScraper,
                private val monitor: Monitor) extends application.JFXApp.PrimaryStage {

  private val sceneSize = 600
  private val offX = 20

  title = stageTitle
  scene = new EventsScene(sceneSize * 2 - offX, sceneSize, this, scraper, monitor)

  onCloseRequest = (ev: WindowEvent) => {
    if (monitor.monitoredEvents().nonEmpty) {
      val alert = new EventsAlert("Events will not be monitored",
        "Data will be lost", MainStage.this, AlertType.Confirmation)

      val result = alert.showAndWait()

      result match {
        case Some(ButtonType.OK) => {
          close()
          System.exit(0)
        }
        case _ => {
          ev.consume()
        }
      }
    } else {
      close()
      System.exit(0)
    }
  }

}
