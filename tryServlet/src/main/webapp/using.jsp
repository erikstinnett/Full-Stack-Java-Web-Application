<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="http://localhost:8081/tryServlet/SimpleServlet" method="get">
		Enter your name: <input type="text" name="textbox1"> <br>
	
		<%
		String selectionText = (String) request.getAttribute("selectionList");
		%>
		Please select a car:<br><%=selectionText%><br> <input
			type="submit" value="process" name="indexButton">
	</form>
</body>
</html>