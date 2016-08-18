<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FirstSTest</title>
</head>
<body>
	<form action="test" method="GET">
		<input type="submit" value="Execute">
	</form>

	<jsp:useBean id="calendar" class="java.util.GregorianCalendar" />
	<form action="timeaction" method="POST">
		<input type="hidden" name="time" value="${calendar.timeInMillis}" />
		<input type="submit" name="button" value="Посчитать время" />
	</form>
	
	<a href="login">Login</a>
	<a href="registration">Registration</a>
	
</body>
</html>
