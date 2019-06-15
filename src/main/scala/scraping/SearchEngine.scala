package scraping

import events.Event

import scala.math.min

object SearchEngine {
  private def LevenshteinDist(a: String, b: String): Int =
    ((0 to b.length).toList /: a)((prev, x) =>
      (prev zip prev.tail zip b).scanLeft(prev.head + 1) {
        case (h, ((d, v), y)) => min(min(h + 1, v + 1), d + (if (x == y) 0 else 1))
      }) last

  private def matches(string: String, pattern: String): Boolean = {
    val a = string.toLowerCase.trim
    val b = pattern.toLowerCase.trim
    val minPrefixLen = 4
    val maxPrefixDist: Int = b.length / 5
    val maxFullDist = b.length / 3
    for (i <- minPrefixLen to a.length){
      if (LevenshteinDist(a.take(i), b) <= maxPrefixDist) return true
    }
    LevenshteinDist(a, b) < maxFullDist
  }

  def searchForEvents(events: List[Event], pattern: String): List[Event] =
    events.filter(e => matches(e.name, pattern))
}
