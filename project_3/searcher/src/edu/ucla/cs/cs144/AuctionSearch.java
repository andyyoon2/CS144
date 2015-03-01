package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.SearchEngine;

public class AuctionSearch implements IAuctionSearch {

	/*
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */

	public SearchResult[] basicSearch(String query, int numResultsToSkip,
			int numResultsToReturn) {
    try{
      SearchEngine se = new SearchEngine();
      TopDocs topDocs = se.performSearch(query, numResultsToReturn + numResultsToSkip);
      ScoreDoc[] hits = topDocs.scoreDocs;

      ArrayList<SearchResult> results = new ArrayList<SearchResult>();
      for (int i = numResultsToSkip; i < hits.length; i++){
        Document doc = se.getDocument(hits[i].doc);
        results.add(new SearchResult(doc.get("id"), doc.get("name")));
      }

      return results.toArray(new SearchResult[results.size()]);
    } catch (IOException e) {
      System.out.println(e);
      return new SearchResult[1];
    } catch (ParseException e) {
      System.out.println(e);
      return new SearchResult[1];
    }
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// We can order search results in any order
    // First do basic keyword search, and do location query on those results
    SearchResult[] basicResults = basicSearch(query, numResultsToSkip, numResultsToReturn);
    Connection conn = null;
    try {
      conn = DbManager.getConnection(true);
      String spatialQuery = "SELECT i.id, i.Name FROM Items i\n" +
                            "INNER JOIN Locations l ON\n" +
                            "i.id = ? AND l.ItemID = i.id\n" +
                            "AND MBRContains(GeomFromText(?), (l.Location));";
      PreparedStatement ps = conn.prepareStatement(spatialQuery);
      String mbr = "Linestring(" +
                   region.getLx() + ' ' + region.getLy() + ',' +
                   region.getRx() + ' ' + region.getLy() + ',' +
                   region.getRx() + ' ' + region.getRy() + ',' +
                   region.getLx() + ' ' + region.getRy() + ',' +
                   region.getLx() + ' ' + region.getLy() + ")";
      ps.setString(2, mbr);

      int id; String name; ResultSet rs;
      ArrayList<SearchResult> results = new ArrayList<SearchResult>();

      // Query each ItemID
      for (int i = 0; i < basicResults.length; i++) {
        ps.setInt(1, Integer.parseInt(basicResults[i].getItemId()));
        rs = ps.executeQuery();
        if (rs.first()) {
          id = rs.getInt("id");
          name = rs.getString("Name");
          results.add(new SearchResult(Integer.toString(id),name));
        }
      }
      conn.close();
      return results.toArray(new SearchResult[results.size()]);
    } catch (SQLException ex) {
      System.out.println(ex);
      return new SearchResult[0];
    }
	}

// Item Struct
class Item {
    public int id, Number_of_Bids;
    public double Currently, Buy_Price, First_Bid, Latitude, Longitude;
    public String Name, Location, Country, SellerID, Description;
    public Timestamp Started, Ends;
}
// Bid Struct
class Bid {
  public int BidderRating;
  public String UserID, Location, Country;
  public Timestamp Time;
  public double Amount;
}

	public String getXMLDataForItemId(String itemId) {
    Item item = new Item();
    ArrayList<String> categories;
    ArrayList<Bid> bids;
    int SellerRating = 0;
    try {
      // Retrieve the item from DB
      Connection conn = DbManager.getConnection(true);
      String query = "SELECT * FROM Items WHERE id = ?;";
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, Integer.parseInt(itemId));
      ResultSet rs = ps.executeQuery();
      if (rs.first()) {
        // Store values
        item.id = rs.getInt("id");
        item.Name = rs.getString("Name");
        item.Currently = rs.getDouble("Currently");
        item.Buy_Price = rs.getDouble("Buy_Price");
        item.First_Bid = rs.getDouble("First_Bid");
        item.Number_of_Bids = rs.getInt("Number_of_Bids");
        item.Location = rs.getString("Location");
        item.Latitude = rs.getDouble("Latitude");
        item.Longitude = rs.getDouble("Longitude");
        item.Country = rs.getString("Country");
        item.Started = rs.getTimestamp("Started");
        item.Ends = rs.getTimestamp("Ends");
        item.SellerID = rs.getString("SellerID");
        item.Description = rs.getString("Description");
      } else {
        return "";
      }
      // Get related data
      // SellerRating
      query = "SELECT SellerRating FROM Users WHERE id = ?;";
      ps = conn.prepareStatement(query);
      ps.setString(1, item.SellerID);
      rs = ps.executeQuery();
      while (rs.next()) {
        SellerRating = rs.getInt("SellerRating");
      }
      // Categories
      query = "SELECT * FROM Categories WHERE ItemID = ?;";
      ps = conn.prepareStatement(query);
      ps.setInt(1, Integer.parseInt(itemId));
      rs = ps.executeQuery();
      categories = new ArrayList<String>();
      while (rs.next()) {
        categories.add(rs.getString("Name"));
      }
      // Bids
      query = "SELECT b.UserID,b.Time,b.Amount,u.Location,u.Country,u.BidderRating " +
              "FROM Bids b, Users u WHERE b.ItemID = ? AND u.id = b.UserID " +
              "ORDER BY b.Amount;";
      ps = conn.prepareStatement(query);
      ps.setInt(1, Integer.parseInt(itemId));
      rs = ps.executeQuery();
      bids = new ArrayList<Bid>();
      while (rs.next()) {
        Bid bid = new Bid();
        bid.BidderRating = rs.getInt("BidderRating");
        bid.UserID = rs.getString("UserID");
        bid.Location = rs.getString("Location");
        bid.Country = rs.getString("Country");
        bid.Time = rs.getTimestamp("Time");
        bid.Amount = rs.getDouble("Amount");
        bids.add(bid);
      }
    } catch (SQLException ex) {
      System.out.println(ex);
      return "";
    }

    // formatters for doubles and dates
    DecimalFormat price_format = new DecimalFormat("#0.00");
    DecimalFormat location_format = new DecimalFormat("#0.000000");
    SimpleDateFormat sqldf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat date_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

    // Populate the XML
		String xml = "<Item ItemID=\"" + Integer.toString(item.id) + "\">\n";
    xml += "\t<Name>" + item.Name + "</Name>\n";
    for (String c : categories) {
      xml += "\t<Category>" + c + "</Category>\n";
    }
    xml += "\t<Currently>$" + price_format.format(item.Currently) + "</Currently>\n";
    if (item.Buy_Price != 0.0) {
      xml += "\t<Buy_Price>$" + price_format.format(item.Buy_Price) + "</Buy_Price>\n";
    }
    xml += "\t<First_Bid>$" + price_format.format(item.First_Bid) + "</First_Bid>\n";
    xml += "\t<Number_of_Bids>" + Integer.toString(item.Number_of_Bids) + "</Number_of_Bids>\n";
    if (item.Number_of_Bids == 0) {
      xml += "\t<Bids />\n";
    } else {
      xml += "\t<Bids>\n";
      for (Bid b : bids) {
        xml += "\t\t<Bid>\n";
        xml += "\t\t\t<Bidder Rating=\"" + Integer.toString(b.BidderRating) + "\" UserID=\"" + b.UserID + "\">\n";
        xml += "\t\t\t\t<Location>" + b.Location + "</Location>\n";
        xml += "\t\t\t\t<Country>" + b.Country + "</Country>\n";
        try {
          xml += "\t\t\t<Time>" + date_format.format(sqldf.parse(b.Time.toString())) + "</Time>\n";
        } catch (Exception e) {
          return "Failed to parse Time";
        }
        xml += "\t\t\t<Amount>$" + price_format.format(b.Amount) + "</Amount>\n";
        xml += "\t\t</Bid>\n";
      }
      xml += "\t</Bids>\n";
    }
    xml += "\t<Location Latitude=\"" + location_format.format(item.Latitude) + "\" Longitude=\"" + location_format.format(item.Longitude)
         + "\">"+ item.Location + "</Location>\n";
    xml += "\t<Country>" + item.Country + "</Country>\n";
    try {
      xml += "\t<Started>" + date_format.format(sqldf.parse(item.Started.toString())) + "</Started>\n";
      xml += "\t<Ends>" + date_format.format(sqldf.parse(item.Ends.toString())) + "</Ends>\n";
    } catch (Exception e) {
      return "Failed to parse Time";
    }
    xml += "\t<Seller Rating=\"" + SellerRating + "\" UserID=\"" + item.SellerID + "\" />\n";
    xml += "\t<Description>" + item.Description + "</Description>\n";
    xml += "</Item>";

    return xml;
	}

	public String echo(String message) {
		return message;
	}

}
