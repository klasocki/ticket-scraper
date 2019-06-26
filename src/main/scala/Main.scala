import scalafx.application.JFXApp
import gui.MainStage
import scraping.{LiveNationScraper, Monitor}

object Main extends JFXApp {
  val scraper: LiveNationScraper = if (parameters.unnamed.nonEmpty) new LiveNationScraper(parameters.unnamed.head)
  else new LiveNationScraper()
  stage = new MainStage("Events monitor", scraper, new Monitor(scraper))
}
