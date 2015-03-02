<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>eBay Item ID Lookup: <%= request.getAttribute("id") %></title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<link href="reset.css" rel="stylesheet" type="text/css" />
<link href="style.css" rel="stylesheet" type="text/css" />
<link href="styles.css" rel="stylesheet" type="text/css" />
<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css' />
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript"
  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCUOAPIKsWsv9GpCCdiW-leH6L0HyR-FyY">
</script>
</head>
<body>
<div id="fb-root"></div>
<div id="wrapper">
  <!-- Header -->
  <div id="header">
    <h5><a href="keywordSearch.html">Search</a> | <a href="getItem.html">Item Lookup</a></h5>
    <img class="logo" alt="" src="img/logo.png" /> </div>
  <!-- end -->
  <div id="content">
    <!-- Any product info -->
    <div class="info">
      <h2>Item ID Lookup</h2>
      <p>
      <form action="item" method="get">
        Item ID: <input type="text" name="id"><br \ >
        <input type="submit" value="Submit">
      </form>
      <br \><br \><br \>
    <b>Item ID Lookup:</b> <%= request.getAttribute("id") %> <br /><br />
    <span id="main">
    <b>Name:</b> <span id="name"></span><br /><br />
    <b>Seller (Rating:<span id="seller_rating"></span>):</b> <span id="seller_id"></span><br /><br />
    <b>Categories:</b> <span id="categories"></span><br /><br />
    <b>Location (Lat: <span id="latitude"></span> Lon: <span id="longitude"></span>):</b> <span id="location"></span><br /><br />
    <b>Country:</b> <span id="country"></span><br /><br /><br />

    <div id="canvas" style="height:400px;width:400px;"></div> <br /><br /><br />

    <b>Description:</b> <span id="description"></span><br /><br /><br />

    <b>Started:</b> <span id="started"></span><br /><br />
    <b>Ends:</b> <span id="ends"></span><br /><br />
    <b>Current Price:</b> <span id="currently"></span><br /><br />
    <b>First Bid:</b> <span id="first_bid"></span><br /><br />
    <b># of Bids:</b> <span id="number_of_bids"></span><br /><br />
    <span id="bids_label"><b>Bids:</b> <span id="bids"></span><br /></span>
    </span>
    <script>
      var xml_string = '<%= request.getAttribute("result") %>';
      if(xml_string.indexOf('Item') == -1){
        $("#main").text("Not Found");
      }
      else{
        xml_string = xml_string.replace(/\t/g, '');
        $xml = $(xml_string);

        var name = $xml.find("Name").text(),
            lat  = $xml.children("Location").attr("latitude"),
            lon  = $xml.children("Location").attr("longitude"),
            loc = $xml.children("Location").text();

        $("#name").text(name);
        $("#seller_rating").text($xml.find("Seller").attr("rating"));
        $("#seller_id").text($xml.find("Seller").attr("userid"));
        $xml.find("Category").each(function(category){
          if($("#categories").text() != "")
            $("#categories").append(" | ");
          $("#categories").append(this);
        });
        $("#latitude").text(lat);
        $("#longitude").text(lon);
        $("#location").text(loc);
        $("#country").text($xml.children("Country").text());

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

      if(bids.length == 0)
        $("#bids_label").hide();

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

      function initialize() {
        var has_location = true;
        if (lat == 0 && lon == 0) {
          has_location = false;
        }
        var latlng = new google.maps.LatLng(lat,lon);
        var myOptions = {
          zoom: 9, // default is 8
          center: latlng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(document.getElementById("canvas"),
            myOptions);

        if (has_location) {
            var marker = new google.maps.Marker({
                position: latlng,
                map: map,
                title: name,
                animation: google.maps.Animation.DROP
            });
        } else {
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode( {'address': loc},
                function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                      map.setCenter(results[0].geometry.location);
                      var marker = new google.maps.Marker({
                          map: map,
                          position: results[0].geometry.location,
                          title: name,
                          animation: google.maps.Animation.DROP
                      });
                      map.setZoom(8);
                    } else {
                      // No result, display world map
                      map.setZoom(2);
                    }
                }
            );
        }
      }
      google.maps.event.addDomListener(window, 'load', initialize);
    }
    </script>
      </p>
    </div>
    <!-- end -->

  </div>

</div>
<!-- License info -->
<div id="license">
  <div class="lic_text"> CS144 Project 4 | Team: Last Minute | By: Andy Yoon & Jiexi Luan</div>
</div>
<!-- end -->
</body>
</html>


