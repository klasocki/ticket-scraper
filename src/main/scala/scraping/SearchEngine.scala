package scraping

import events.Event

import scala.math.min

/**
  * Custom search engine to work with events. Supports regular expressions and
  * full string or prefix (of minimum length 4) matching using Levenshtein distance.
  * Allows for one typo per 3 characters when matching full string, or one typo per 5 characters
  * when matching prefix
  */
object SearchEngine {
  /**
    * Searches for events with SearchEngine matching logic
    * @param events List of events to search in
    * @param pattern Pattern to search for
    * @return List of events from events matching pattern
    */
  def searchForEvents(events: List[Event], pattern: String): List[Event] =
    events.filter(e => matches(e.name, pattern))

  private def LevenshteinDist(a: String, b: String): Int =
  ((0 to b.length).toList /: a)((prev, x) =>
  (prev zip prev.tail zip b).scanLeft(prev.head + 1) {
        case (h, ((d, v), y)) => min(min(h + 1, v + 1), d + (if (x == y) 0 else 1))
      }) last

  private def matches(string: String, pattern: String): Boolean = {
    val a = string.toLowerCase.trim
    val b = pattern.toLowerCase.trim
    val maxFullDist = b.length / 3
    string.matches(pattern) || LevenshteinDist(a, b) < maxFullDist || anyPrefixMatches(a, b)
  }

  private def anyPrefixMatches(string: String, pattern: String): Boolean = {
    val minPrefixLen = 4
    val maxPrefixDist: Int = pattern.length / 5
    (minPrefixLen to string.length).toList.exists(i => LevenshteinDist(string.take(i), pattern) <= maxPrefixDist)
  }
}
