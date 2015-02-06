/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


// Item Struct
class Item {
  public String id, Name, Currently, Buy_Price, First_Bid, Number_of_Bids,
                Location, Latitude, Longitude, Country, Started, Ends, SellerID,
                Description;
}

// Category Struct
class Category {
  public String ItemID, Name;
}

// Bid Struct
class Bid {
  public String ItemID, UserID, Time, Amount;
}

// User Struct
class User {
  public String id, Location, Country, BidderRating, SellerRating;
}

class MyParserPrint {

    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };

    static class MyErrorHandler implements ErrorHandler {

        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }

        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }

        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }

    }

    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }

    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }

    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem).replace(",", "&#44;").replaceAll("\"","&quote;").replaceAll("(\\r|\\n|\\r\\n)+", "&#10;");
        else
            return "";
    }

    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile, Map<String, Item> items, Map<String, Category> categories,
                            Map<String, Bid> bids, Map<String, User> users) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }

        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);

        /* Fill in code here (you will probably need to write auxiliary
            methods). */

        parseItems(doc.getElementsByTagName("Item"), items, categories, bids, users);

        /**************************************************************/

        //recursiveDescent(doc, 0);

    }


    public static void parseItems(NodeList nlist, Map<String, Item> items, Map<String, Category> categories,
                                  Map<String, Bid> bids, Map<String, User> users) {

      for(int i=0; i<nlist.getLength(); i++){
        parseItem(nlist.item(i), items, categories, bids, users);
      }

    }

    public static void parseItem(Node n,  Map<String, Item> items, Map<String, Category> categories,
                                  Map<String, Bid> bids, Map<String, User> users) {

      Element e = (Element)n;
      Item item= new Item();

      item.id = e.getAttribute("ItemID");
      item.Name = getElementTextByTagNameNR(e, "Name");
      item.Currently = strip(getElementTextByTagNameNR(e, "Currently"));
      item.Buy_Price = strip(getElementTextByTagNameNR(e, "Buy_Price"));
      item.First_Bid = strip(getElementTextByTagNameNR(e, "First_Bid"));
      item.Number_of_Bids = getElementTextByTagNameNR(e, "Number_of_Bids");
      item.Location = getElementTextByTagNameNR(e, "Location");
      item.Latitude = getElementByTagNameNR(e, "Location").getAttribute("Latitude");
      item.Longitude = getElementByTagNameNR(e, "Location").getAttribute("Longitude");
      item.Country = getElementTextByTagNameNR(e, "Country");
      item.Started = parse_time(getElementTextByTagNameNR(e, "Started"));
      item.Ends = parse_time(getElementTextByTagNameNR(e, "Ends"));
      item.SellerID = getElementByTagNameNR(e, "Seller").getAttribute("UserID");
      item.Description = getElementTextByTagNameNR(e, "Description");

      if(item.Description.length() > 4000)
        item.Description = item.Description.substring(0, 4000);
      if (item.Description.endsWith("\\"))
          item.Description = item.Description.substring(0,item.Description.length()-1);


      String SellerRating = getElementByTagNameNR(e, "Seller").getAttribute("Rating");

      /*
      System.out.println(item.id + ", " + item.Name + ", " + item.Currently + ", " + item.Buy_Price + ", " +
                         item.First_Bid + ", " + item.Number_of_Bids + ", " + item.Location + ", " +
                         item.Latitude + ", " + item.Longitude + ", " + item.SellerID + ", " +
                         item.Country + ", " + item.Started + ", " + item.Ends + ", " + item.Description);
      */

      items.put(item.id, item);

      // Categories processing
      Element[] category_elements = getElementsByTagNameNR(e, "Category");

      for(int i = 0; i < category_elements.length; i++) {
        parseCategory(category_elements[i], item.id, categories);
      }

      // Seller Processing
      parseSeller(item.SellerID, SellerRating, users);

      // Bids Processing
      Element[] bid_elements = getElementsByTagNameNR(getElementByTagNameNR(e, "Bids"), "Bid");

      for(int i = 0; i < bid_elements.length; i++) {
        parseBid(bid_elements[i], item.id, bids, users);
      }

      //org.w3c.dom.NodeList nlist = n.getChildNodes();

      //for(int i=0; i<nlist.getLength(); i++){
       // recursiveDescent(nlist.item(i), 0);
      //}
    }


    public static void parseCategory(Element e, String ItemID, Map<String, Category> categories) {

      Category category = new Category();
      category.ItemID = ItemID;
      category.Name = getElementText(e);

      categories.put(category.ItemID + category.Name, category);

      //System.out.println("Category: " + category.ItemID + " " + category.Name);
    }


    public static void parseSeller(String UserID, String SellerRating, Map<String, User> users) {

      User user = users.get(UserID);
      if(user == null) {
        user = new User();
        user.id = UserID;
      }

      user.SellerRating = SellerRating;

      users.put(UserID, user);

      //System.out.println("Seller: " + user.id + " " + user.SellerRating);

    }

    public static void parseBid(Element e, String ItemID, Map<String, Bid> bids, Map<String, User> users){

      Element bidder = getElementByTagNameNR(e, "Bidder");
      String UserID = bidder.getAttribute("UserID");
      String BidderRating = bidder.getAttribute("Rating");
      String Location = getElementTextByTagNameNR(bidder, "Location");
      String Country = getElementTextByTagNameNR(bidder, "Country");
      String Time = parse_time(getElementTextByTagNameNR(e, "Time"));
      String Amount = strip(getElementTextByTagNameNR(e, "Amount"));

      Bid bid = new Bid();
      bid.ItemID = ItemID;
      bid.UserID = UserID;
      bid.Time = Time;
      bid.Amount = Amount;

      bids.put(ItemID + UserID + Time, bid);

      User user = users.get(UserID);
      if (user == null){
        user = new User();
        user.id = UserID;
      }

      user.BidderRating = BidderRating;
      user.Location = Location;
      user.Country = Country;

      users.put(UserID, user);

      //System.out.println("Bidder: " + bid.UserID + " " + bid.Time + " " + bid.Amount);
      //System.out.println("User: " + user.id + " " + user.Location + " " + user.Country + " " + user.BidderRating);

    }

    public static String parse_time(String time_string){
      try {
        Date date = new Date(time_string);

        SimpleDateFormat sqldf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String parsed_time = sqldf.format(date);

        return parsed_time;
      }
      catch (Exception e) {
        return "";
      }
    }


    public static void recursiveDescent(Node n, int level) {
        // adjust indentation according to level
        for(int i=0; i<4*level; i++)
            System.out.print(" ");

        // dump out node name, type, and value
        String ntype = typeName[n.getNodeType()];
        String nname = n.getNodeName();
        String nvalue = n.getNodeValue();

        System.out.println("Type = " + ntype + ", Name = " + nname + ", Value = " + nvalue);

        // dump out attributes if any
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
        if(nattrib != null && nattrib.getLength() > 0)
            for(int i=0; i<nattrib.getLength(); i++)
                recursiveDescent(nattrib.item(i),  level+1);

        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();

        for(int i=0; i<nlist.getLength(); i++)
            recursiveDescent(nlist.item(i), level+1);
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }

        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        }
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }

        // Create HashMaps for us to write structs into to be processed later
        Map<String, Item> items = new HashMap<String, Item>();
        Map<String, Category> categories = new HashMap<String, Category>();
        Map<String, Bid> bids = new HashMap<String, Bid>();
        Map<String, User> users = new HashMap<String, User>();

        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile, items, categories, bids, users);
        }

        // Parse all of our HashMaps and output them to some files
        try {
          //items
          PrintWriter writer = new PrintWriter("items.dat", "UTF-8");

          for(Item item : items.values()){
            String line = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                                        item.id, item.Name, item.Currently, item.Buy_Price, item.First_Bid,
                                        item.Number_of_Bids, item.Location, item.Latitude, item.Longitude,
                                        item.Country, item.Started, item.Ends, item.SellerID, item.Description);
            writer.println(line);
          }
          writer.close();

          //categories
          writer = new PrintWriter("categories.dat", "UTF-8");
          for(Category category : categories.values()){
            String line = String.format("\"%s\",\"%s\"", category.ItemID, category.Name);
            writer.println(line);
          }
          writer.close();


          //bids
          writer = new PrintWriter("bids.dat", "UTF-8");
          for(Bid bid : bids.values()){
            String line = String.format("\"%s\",\"%s\",\"%s\",\"%s\"", bid.ItemID, bid.UserID, bid.Time, bid.Amount);
            writer.println(line);
          }
          writer.close();


          //users
          writer = new PrintWriter("users.dat", "UTF-8");
          for(User user : users.values()){
            if(user.Location == null)
              user.Location = "";
            if(user.Country == null)
              user.Country = "";
            if(user.BidderRating == null)
              user.BidderRating = "0";
            if(user.SellerRating == null)
              user.SellerRating = "0";
            String line = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", user.id, user.Location, user.Country,
                                                                      user.BidderRating, user.SellerRating);
            writer.println(line);
          }
          writer.close();

      }
      catch (Exception e){
        e.printStackTrace();
      }
    }
}
