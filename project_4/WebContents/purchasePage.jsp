<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>eBay Item Purchase: <%= request.getAttribute("id") %></title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<link href="reset.css" rel="stylesheet" type="text/css" />
<link href="style.css" rel="stylesheet" type="text/css" />
<link href="styles.css" rel="stylesheet" type="text/css" />
<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css' />
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
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
      <h2>Purchase</h2>
      <p>
          <form action='<%= request.getAttribute("post_url") %>' method="get">
            <b>ItemID:</b> <span id="itemId"><%= request.getAttribute("id") %></span><br /><br />
            <b>Item Name:</b> <span id="name"></span><br /><br />
            <b>Buy Price:</b> <span id="buy_price"></span><br /><br />
            <b>Credit Card Number:</b> <input type="text" name="card" autocomplete="off"><br /><br />
            <input type="submit" value="Submit">
          </form>
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
<script>
  var xml_string = '<%= request.getAttribute("result") %>';
  console.log(xml_string);
  if(xml_string.indexOf('Item') == -1){
    $("#main").text("Not Found");
  } else {
    xml_string = xml_string.replace(/\t/g, '');
    $xml = $(xml_string);

    $("#name").text($xml.find("Name").text());
    $("#buy_price").text($xml.find("Buy_price").text());
  }
</script>
</body>
</html>