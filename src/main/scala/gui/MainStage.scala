package gui

import scalafx.application
import scraping.{LiveNationScraper, Monitor}

class MainStage(private val stageTitle: String, private val scraper: LiveNationScraper,
                private val monitor: Monitor) extends application.JFXApp.PrimaryStage {

  private val sceneSize = 600
  private val offX = 20

  title = stageTitle
  scene = new EventsScene(sceneSize * 2 - offX, sceneSize, this, scraper, monitor)

}
