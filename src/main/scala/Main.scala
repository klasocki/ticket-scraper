import java.io.FileNotFoundException

import scalafx.application.JFXApp
import gui.MainStage
import scraping.{LiveNationScraper, Monitor}

object Main extends JFXApp {
  try {
    val scraper: LiveNationScraper = if (parameters.unnamed.nonEmpty) new LiveNationScraper(parameters.unnamed.head)
    else new LiveNationScraper()
    stage = new MainStage("Events monitor", scraper, new Monitor(scraper))
  } catch {
    case e: FileNotFoundException =>
      println(e.getMessage)
      System.exit(1)
  }
}
