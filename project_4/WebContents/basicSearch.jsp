<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Basic Search (skip: <%= request.getAttribute("skip") %>, return: <%= request.getAttribute("return") %>): <%= request.getAttribute("query") %></title>
</head>
<body>
    Basic Search (skip: <%= request.getAttribute("skip") %>, return: <%= request.getAttribute("return") %>): <%= request.getAttribute("query") %> <br>
    Results (found: <%= request.getAttribute("length") %>): <br>
    <table>
      <c:forEach items="${results}" var="result">
          <tr>
              <td><c:out value="${result.itemId}"/>:</td>
              <td><c:out value="${result.name}"/></td>
          </tr>
      </c:forEach>
    </table>
</body>
</html>