<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>ebay Keyword Search: <%= request.getAttribute("query") %></title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<link href="reset.css" rel="stylesheet" type="text/css" />
<link href="style.css" rel="stylesheet" type="text/css" />
<link href="styles.css" rel="stylesheet" type="text/css" />
<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css' />
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script src="AutoSuggestControl.js"></script>
<script src="SuggestionProvider.js"></script>
<script>
  window.onload = function () {
    var oTextbox = new AutoSuggestControl(document.getElementById("autosuggest"), new SuggestionProvider());
  }
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
      <h2>Search</h2>
      <p>
          <form action="search" method="get">
            Keywords: <input id="autosuggest" type="text" name="q" autocomplete="off"><br />
            <input type="hidden" name="numResultsToSkip" value="0">
            # of Results to return: <select name="numResultsToReturn">
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="30">30</option>
                                    <option value="40">40</option>
                                    <option value="50">50</option>
                                  </select><br>
            <input type="submit" value="Submit">
          </form><br /><br /><br />
    Search (skip: <%= request.getAttribute("skip") %>, return: <%= request.getAttribute("return") %>): <%= request.getAttribute("query") %> <br /><br />
    Results (found: <%= request.getAttribute("length") %>): <br /><br />
      </p>
    <ul class="get">
      <c:forEach items="${results}" var="result">
        <li>
            <a href='item?id=<c:out value="${result.itemId}"/>'><c:out value="${result.itemId}"/></a>:
            <a href='item?id=<c:out value="${result.itemId}"/>'><c:out value="${result.name}"/></a>
        </li>
      </c:forEach>
    </ul>
    <p>
  <%
    Integer amount_to_skip = (Integer)request.getAttribute("skip") - (Integer)request.getAttribute("return");
    if(amount_to_skip < 0) amount_to_skip = 0;
    if((Integer)request.getAttribute("skip") > 0)
    out.println("<a href=\"?numResultsToSkip=" + amount_to_skip +"&q="+request.getAttribute("query")+"&numResultsToReturn="+request.getAttribute("return")+"\">Previous</a>");
  %>
  &nbsp;&nbsp;&nbsp;
  <% System.out.println((Integer)request.getAttribute("skip")); %>
  <% if((Integer)request.getAttribute("length") == (Integer)request.getAttribute("return"))
    out.println("<a href=\"?numResultsToSkip=" + ((Integer)request.getAttribute("skip") + (Integer)request.getAttribute("return")) +"&q="+request.getAttribute("query")+"&numResultsToReturn="+request.getAttribute("return")+"\">Next</a>");
   %>
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

