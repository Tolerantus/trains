<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="resources/Validation Error.css">
<link href='http://fonts.googleapis.com/css?family=PT+Sans&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="resources/StationAdding.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Add new stations</title>
</head>
<body>

<c:if test="${user!=null}">
<c:if test="${admin==true }">
<div id="top-menu">
	<div class="user"><img alt="" src="resources/images/1.png"><span class="user"><%=session.getAttribute("user")%></span></div>
	<div >
		<div id="menu">
			<c:url var="menuURL" value="/menu"/>
			<a href="${menuURL }"><img alt="" src="resources/images/home.png"></a>
		</div>
	</div>
</div>
<h1>Add stations</h1>
<div class="route" align="center">
<img alt="" src="resources/images/start.png" class="flag">
	<ul>
		<c:forEach var="st" items="${sessionScope.newRoute}">
					<li><b>${st}</b></li>
		</c:forEach>
	</ul>
	<img alt="" src="resources/images/stop.png" class="flag">
</div>
<div align="center">
<c:url var="insertStationURL" value="/newRoute/newStartAndFinish/newStation"/>
<form action="${insertStationURL }" method="post" name="add" id="addForm">
<input type="hidden" name="action" value="INSERT_STATION">
	
	<div id="selector">
				<select id="step" name="step">
					<option disabled>step</option>
					<%List<String> stations = (List<String>)session.getAttribute("newRoute"); %>
					<%for (int i=0; i<stations.size()-1; i++){%>
					<option selected><%=i %></option>
					<%} %>
				</select>
			
				<input type="text" name="newStation" id="typed" placeholder="station">
			
			<c:if test="${allStations !=null}">
		
				<select id="stations" name = "stations">
					<option disabled="disabled">station</option>
					<c:forEach var="st" items="${allStations}">
						<option selected>${st}</option>
					</c:forEach>
				</select>
			
			</c:if>
		
	</div>	
</form >
</div>
<c:url var="inputTimeURL" value="/newRoute/newStartAndFinish/timeAndCost"/>
<form action="${inputTimeURL }" method="post" name="getTime">
	<input type="hidden" name="action" value="GET_TIME_AND_COST">
</form>
	
<div class="actions">
	<div align="center">
			<input type="button" class="submit" value="Add station" onclick="validate()">
	</div>

	<div align="center">
			<input type="button" class="submit" value="Stop" onclick="document.forms['getTime'].submit()">
	</div>
</div>	
	
	
<script type="text/javascript" src = "resources/StationAdding.js"></script>
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