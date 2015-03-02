<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Basic Search (skip: <%= request.getAttribute("skip") %>, return: <%= request.getAttribute("return") %>): <%= request.getAttribute("query") %></title>
    <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
    <script src="AutoSuggestControl.js"></script>
    <script src="SuggestionProvider.js"></script>
    <script>
      window.onload = function () {
        var oTextbox = new AutoSuggestControl(document.getElementById("autosuggest"), new SuggestionProvider());
      }
    </script>
    <link rel="stylesheet" type="text/css" href="styles.css" />
</head>
<body>
      <h1>eBay Keyword Search</h1>
      <form action="search" method="get">
        Keywords: <input id="autosuggest" type="text" name="q"><br />
        <input type="hidden" name="numResultsToSkip" value="0">
        # of Results to return: <select name="numResultsToReturn">
                                <option value="10">10</option>
                                <option value="20">20</option>
                                <option value="30">30</option>
                                <option value="40">40</option>
                                <option value="50">50</option>
                              </select><br />
        <input type="submit" value="Submit">
      </form><br /><br /><br />
    Basic Search (skip: <%= request.getAttribute("skip") %>, return: <%= request.getAttribute("return") %>): <%= request.getAttribute("query") %> <br /><br />
    Results (found: <%= request.getAttribute("length") %>): <br />
    <table>
      <c:forEach items="${results}" var="result">
        <tr>
            <td><a href='item?id=<c:out value="${result.itemId}"/>'><c:out value="${result.itemId}"/></a>:</td>
            <td><a href='item?id=<c:out value="${result.itemId}"/>'><c:out value="${result.name}"/></a></td>
        </tr>
      </c:forEach>
    </table>
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
</body>
</html>