import scalafx.application.JFXApp
import gui.MainStage
import scraping.{LiveNationScraper, Monitor}
object Main extends JFXApp {

  val scraper =new LiveNationScraper("https://www.livenation.pl/event/allevents?page=")
  stage = new MainStage("Events monitor",scraper,new Monitor(scraper))

}
