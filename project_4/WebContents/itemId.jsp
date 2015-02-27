<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Item ID Loopup: <%= request.getAttribute("id") %></title>
    <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
</head>
<body>
    <h1>eBay Item ID Lookup</h1>
    <form action="item" method="get">
      Item ID: <input type="text" name="id"><br>
      <input type="submit" value="Submit">
    </form><br \><br \><br \>
    Item ID Lookup: <%= request.getAttribute("id") %> <br /><br />
    <span id="main">
    Name: <span id="name"></span><br />
    Seller (Rating:<span id="seller_rating"></span>): <span id="seller_id"></span><br />
    Categories: <span id="categories"></span><br />
    Location (Lat: <span id="latitude"></span> Lon: <span id="longitude"></span>): <span id="location"></span><br />
    Country: <span id="country"></span><br /><br /><br />

    Description: <span id="description"></span><br /><br /><br />

    Started: <span id="started"></span><br />
    Ends: <span id="ends"></span><br />
    Current Price: <span id="currently"></span><br />
    First Bid: <span id="first_bid"></span><br />
    # of Bids: <span id="number_of_bids"></span><br />
    Bids: <span id="bids"></span><br />
    </span>
    <script>
      var xml_string = '<?xml version="1.0" encoding="utf-8"?><%= request.getAttribute("result") %>';
      if(xml_string == '<?xml version="1.0" encoding="utf-8"?>'){
        $("#main").text("Not Found");
      }
      else{
        xml_string = xml_string.replace(/\t/g, '');
        $xml = $(xml_string);

        $("#name").text($xml.find("Name").text());
        $("#seller_rating").text($xml.find("Seller").attr("rating"));
        $("#seller_id").text($xml.find("Seller").attr("userid"));
        $xml.find("Category").each(function(category){
          if($("#categories").text() != "")
            $("#categories").append(" | ");
          $("#categories").append(this);
        });
        $("#latitude").text($xml.find("Location").attr("latitude"));
        $("#longitude").text($xml.find("Location").attr("longitude"));
        $("#location").text($xml.find("Location").text());
        $("#country").text($xml.find("Country").text());

        $("#description").text($xml.find("Description").text());

        $("#started").text($xml.find("Started").text());
        $("#ends").text($xml.find("Ends").text());
        $("#currently").text($xml.find("Currently").text());
        $("#first_bid").text($xml.find("First_Bid").text());
        $("#number_of_bids").text($xml.find("Number_of_Bids").text());

        var bids = []
        $xml.find("Bids Bid").each(function(bid){
          var data = {
            bidder_rating: $(this).find("Bidder").attr("Rating"),
            bidder_id: $(this).find("Bidder").attr("UserID"),
            Location: $(this).find("Location").text(),
            Country: $(this).find("Country").text(),
            Time: $(this).find("Time").text(),
            Amount: $(this).find("Amount").text()
          };
          bids.push(data);
        });
      function sort_time(a, b){
        return ((a.Time > b.Time) ? -1 : (a.Time < b.Time) ? 1 : 0);
      }
      bids = bids.sort(sort_time);

      $.each(bids, function(_, bid) {
        var bid_html = "<div style='margin-left:20px;'>";
        bid_html += "Bidder (Rating: " + bid.bidder_rating + "): " + bid.bidder_id;
        bid_html += "<br />";
        bid_html += "Location: " + bid.Location;
        bid_html += "<br />";
        bid_html += "Country: " + bid.Country;
        bid_html += "<br />";
        bid_html += "Time: " + bid.Time;
        bid_html += "<br />";
        bid_html += "Amount: " + bid.Amount;
        bid_html += "<br />";
        bid_html += "</div>";
        bid_html += "<br />";
        $("#bids").append(bid_html);
      });
    }
    </script>
</body>
</html>