Team: Last Minute

basicSearch() and spatialSearch() were both fairly straightforward. 
In basicSearch(), we essentially open up the single lucene index we created previously
and perform a search based on the arguments given.

In spatialSearch(), since we can return the results in any order, we do a basic
search on the given query to get an array of SearchResults, and then do a
spatial query on the database with those results.

For getXMLDataForItemId(), we used Item and Bid structs to make storing
variables easier. We query the database and store all the values we need for
the item, then build the XML representation using string concatenation.