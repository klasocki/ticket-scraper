# ticket-scraper
Scala course project - Live Nation ticket scraper, searching for events, monitoring sold-out shows and notifying about new tickets
### Instructions
1. To run program you have to execute .jar file, 
2. Executing it without arguments will use https://www.livenation.pl/event/allevents url to find events,
3. Optional argument is path for .html local file which scraper can use instead of web page,
4. There is already one file in src/test/mocks folder, so you can run program with
 "src/test/mocks/page-to-monitor" argument,
5. This file contains list of events with missing tickets for James Bay,which can be monitored,
6. In "src/test/mocks/allevents" there are 2 files with the same list of events, but in with-tickets1.html has 
  avalailble tickets for James Bay,
7. When "src/test/mocks/page-to-monitor.html" file's content is replaced with previously mentioned file
  program will find tickets and inform you with alarm sound and open browser for this event. 
### Features
1. Program won't allow to monitor events with tickets or monitor event which is already on monitored list.
2. Program will inform you that you have monitored tickets on your list if you are trying to close it 
  having non-empty monitored list.
3. You can open web page for any selected event from left list. 
4. You can search for events and use button to show list of all events after searching. 
