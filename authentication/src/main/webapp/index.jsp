<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html ng-app="mainApp">
<head>
<title>Handmade Shortener</title>
<link rel="stylesheet" type="text/css"
	href="./resources/css/stylesheet.css" />

<script src="./resources/js/jquery/3.1.0/jquery.js"></script>
<script src="./resources/js/angularjs/1.5.8/angular.js"></script>
<script src="./resources/js/angularjs/1.5.8/angular-route.js"></script>
<script src="./resources/js/angularjs/1.5.8/angular-cookies.js"></script>
</head>
<body>

	<div>
		<menu-panel></menu-panel>
		<div id="appwindow" ng-view></div>
		
		<script src="./resources/app/app.module.js"></script>
		<script src="./resources/app/app.config.js"></script>
		
		<script src="./resources/app/menu-panel/menu-panel.module.js"></script>
		<script src="./resources/app/menu-panel/menu-panel.component.js"></script>
		
		<script src="./resources/app/view-urls/view-urls.module.js"></script>
		<script src="./resources/app/view-urls/view-urls.component.js"></script>
		
		<script src="./resources/app/login-panel/login-panel.module.js"></script>
		<script src="./resources/app/login-panel/login-panel.component.js"></script>
		
		<script src="./resources/app/feedback-panel/feedback-panel.module.js"></script>
		<script src="./resources/app/feedback-panel/feedback-panel.component.js"></script>
		
		<script src="./resources/app/logout/logout.module.js"></script>
		<script src="./resources/app/logout/logout.component.js"></script>
		
		<script src="./resources/app/url/url.module.js"></script>
		<script src="./resources/app/url/url.component.js"></script>
		
		<script src="./resources/app/create-url/create-url.module.js"></script>
		<script src="./resources/app/create-url/create-url.component.js"></script>
		
		<script src="./resources/app/tag-urls/tag-urls.module.js"></script>
		<script src="./resources/app/tag-urls/tag-urls.component.js"></script>

		<script src="./resources/app/registration-panel/registration-panel.module.js"></script>
		<script src="./resources/app/registration-panel/registration-panel.component.js"></script>

		<script src="./resources/app/core/authentication/authentication.module.js"></script>
		<script src="./resources/app/core/authentication/authentication.service.js"></script>
		
		<script src="./resources/app/core/url/url.module.js"></script>
		<script src="./resources/app/core/url/url.service.js"></script>
		
		<script src="./resources/app/core/tag/tag.module.js"></script>
		<script src="./resources/app/core/tag/tag.service.js"></script>
		
		<script src="./resources/app/core/registration/registration.module.js"></script>
		<script src="./resources/app/core/registration/registration.service.js"></script>
	</div>
	
</body>
</html>