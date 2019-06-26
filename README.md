# ticket-scraper
Scala course project - Live Nation ticket scraper, searching for events, monitoring sold-out shows and notifying about new tickets
### Run instructions
1. To run the program you have to clone this repository and execute src/main/scala/Main, with all the dependencies listed in build.sbt, or execute a .jar file 
2. Executing without arguments will use https://www.livenation.pl/event/allevents url to find events
3. Optional argument is a path for a local file which scraper can use instead of web page, usually used for testing
4. There is already one file ready in src/test/mocks folder, so you can run the program with
 "src/test/mocks/page-to-monitor.html" argument
5. This file contains list of events with missing tickets for James Bay, which can be monitored
6. In "src/test/mocks/allevents" there are 2 files with the same list of events, but in with-tickets.html there are available tickets for James Bay
7. When "src/test/mocks/page-to-monitor.html" file's content is replaced  at runtime with previously mentioned file,
  the program will find tickets and inform you with alarm sound and open the default browser for this event
### Main features
1. Program won't allow monitoring events with tickets available, or monitor an event twice
2. Program will warn you if you want to exit while still monitoring events (it does not work in background)
3. You can open web page for any selected event from the list 
4. You can search for events, or show all available events. Searching supports regular expressions, and uses Levenshtein distance to allow for one typo per 3 characters when matching full event name, or one typo per 5 characters when matching a prefix (of minimum length 4). So, for instance, you don't need to type "James Bay"; "james" or even "jamers" will be enough
