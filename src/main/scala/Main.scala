import scalafx.application.JFXApp
import gui.MainStage
import scraping.{LiveNationScraper, Monitor}

object Main extends JFXApp {

  val scraper = new LiveNationScraper("src/test/docs/page-to-monitor")
  stage = new MainStage("Events monitor", scraper, new Monitor(scraper))

}
