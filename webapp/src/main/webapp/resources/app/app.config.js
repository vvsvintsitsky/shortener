mainApp.config(
		[ '$routeProvider', '$httpProvider',
				function($routeProvider, $httpProvider) {
					$routeProvider.

					when('/login', {
						template : '<login-panel></login-panel>'
					}).

					when('/viewUrls', {
						template : '<view-urls></view-urls>'
					}).

					when('/info/:urlToView', {
						template : '<url></url>'
					}).

					when('/tag/:tagDescription', {
						template : '<tag-urls></tag-urls>'
					}).

					when('/logout', {
						template : '<logout></logout>'
					}).

					when('/register', {
						template : '<registration-panel></registration-panel>'
					}).
					
					otherwise({
						redirectTo : '/login'
					});

				} ]).run(function($rootScope, $cookies, $location) {
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
		if ($cookies.get('Authentication') == null) {
				if($location.url() == "/viewUrls") {
					$location.path("/login");
				}
				if($location.url() == "/logout") {
					$location.path("/login");
				}
		} else {
			if($location.url() == "/login") {
				$location.path("viewUrls");
			}
		}
		
	});
});