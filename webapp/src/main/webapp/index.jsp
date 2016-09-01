<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Index Page</title>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/test.buttons.js" />"></script>
<script src="<c:url value="/resources/js/main.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/stylesheet.css" />" />
</head>
<body>

	<button type="button" onclick="RestGet()">Method GET</button>
	<button type="button" onclick="RestPost()">Method POST</button>
	<button type="button" onclick="RestDelete()">Method DELETE</button>
	<button type="button" onclick="RestPut()">Method PUT</button>

	<form action="auth" method="post">
		Login:<br/>
		<input type="text" name="login"/><br/>
		Password:<br/>
		<input type="password" name="password"/><br/>
		<input type="submit" value="Log in!"/>
	</form>

	<button type="button" onclick="replaceBody()">Replace body</button>
	<br />

	<a href="jsp/main.jsp">Main Page</a>
</body>
</html>