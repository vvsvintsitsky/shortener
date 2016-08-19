<%@ page isErrorPage="true" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error Page</title>
</head>
<body>
	Request from ${pageContext.errorData.requestURI} if failed<br/>
	Servlet name or type: ${pageContext.errorData.servletName}<br/>
	Status code: ${pageContext.errorData.statusCode}<br/>
	Exception: ${pageContext.errorData.throwable}<br/> 
</body>
</html>