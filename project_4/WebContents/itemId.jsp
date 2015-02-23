<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Item ID Loopup: <%= request.getAttribute("id") %></title>
</head>
<body>
    Item ID Lookup: <%= request.getAttribute("id") %> <br>
    <%= request.getAttribute("result") %>
</body>
</html>