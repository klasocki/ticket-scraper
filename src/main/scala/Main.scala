import scraping.{LiveNationScraper, Monitor}

object Main {
  def main(args: Array[String]): Unit = {
    println("Live Nation ticket scraper")
    val scraper = new LiveNationScraper("src/test/docs/page-to-monitor")
    val monitor = new Monitor(scraper)
    for (elem <- scraper.searchForEvents(" james")) {
      monitor.startMonitoring(elem)
    }
  }
}
