package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();

		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);

		String query = "superman";
		SearchResult[] basicResults = as.basicSearch(query, 0, 20);
		System.out.println("Basic Search Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}


		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38);
		SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
		System.out.println("Spatial Search: camera");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		spatialResults = as.spatialSearch("superman", region, 0, 200);
		System.out.println("Spatial Search: superman");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}

		//String itemId = "1497595357";
		String itemId = "1046983042";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Add your own test here

		System.out.println("RUNNING OUR CUSTOM TESTS NOW -----------");

		query = "superman";
		basicResults = as.basicSearch(query, 0, 10000);
		System.out.println("Basic Search Query(count): " + query + ", expected:68, got:" + basicResults.length);

		query = "kitchenware";
		basicResults = as.basicSearch(query, 0, 10000);
		System.out.println("Basic Search Query(count): " + query + ", expected:1462, got:" + basicResults.length);

		query = "star trek";
		basicResults = as.basicSearch(query, 0, 10000);
		System.out.println("Basic Search Query(count): " + query + ", expected:770, got:" + basicResults.length);
	}
}
