import scraping.LiveNationScraper

object Main {
  def main(args: Array[String]): Unit = {
    println("Live Nation ticket scraper")
    val scraper = new LiveNationScraper("https://www.livenation.pl")
    for (elem <- scraper.getEventList()) {
      println("Tytul: " + elem)
    }
  }
}
