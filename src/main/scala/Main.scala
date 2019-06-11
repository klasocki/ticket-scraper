import scraping.LiveNationScraper
import scraping.SearchEngine

object Main {
  def main(args: Array[String]): Unit = {
    println("Live Nation ticket scraper")
    val scraper = new LiveNationScraper("https://www.livenation.pl")
    for (elem <- SearchEngine.searchForEvents(scraper.getEventList, "Impact")) {
      println(elem)
    }
  }
}
