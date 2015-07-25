<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.util.List" %>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="resources/NewRouteInfo.css">
<link rel="stylesheet" href="resources/Validation Error.css">
<link href='http://fonts.googleapis.com/css?family=PT+Sans&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>New route</title>
</head>
<body>
<c:if test="${user!=null}">
<c:if test="${admin==true }">
<div class="user"><img alt="" src="resources/images/1.png"><span class="user"><%=session.getAttribute("user")%></span></div>
<div >
	<div id="menu">
		<c:url var="menuURL" value="/menu"/>
		<a href="${menuURL }"><img alt="" src="resources/images/home.png"></a>
	</div>
</div>
<h1>New route created!</h1>
<%List<String> data = (List<String>)session.getAttribute("info"); %>
<table class="t">
<thead>
	<tr>
		<th>
			Route name
		</th>
		<th>
			Hours
		</th>
		<th>
			Minutes
		</th>
		<th>
			Overall cost
		</th>
		
	</tr>
</thead>
	<tr>
		
			<td>
				<%=data.get(0) %>
			</td>
			<td>
				<%=data.get(1) %>
			</td>
			<td>
				<%=data.get(2) %>
			</td>
			<td>
				<%=data.get(3) %>
			</td>
	</tr>
</table>
<table class="t">
<tr>
	<td>	
	<b>Stations</b>
	</td>
</tr>
<%for (int i=4; i<data.size(); i++){ %>
<tr>
<td>
<%=data.get(i) %>
</td>
</tr>
<%} %>
</table>

	</c:if>
<c:if test="${admin==false }">
<h3 align="center" style="color:red">You do not have permission to view this page!</h3>
	<form action="${menuURL }">
	<input type="submit" class="submit" value="Login">
	</form>
</c:if>
</c:if>
<c:if test="${user==null}">
	<h1 align="center" style="color:red">Unregistered user cannot look through this page!</h1>
	<div align="center">
		<c:url var="loginURL" value="/login"/>
		<form action="${loginURL }">
			<input type="submit" class="submit" value="Login">
		</form>
	</div>
</c:if>	
</body>
</html>