mainApp.config(
		[ '$routeProvider', '$httpProvider',
				function($routeProvider, $httpProvider) {
					$routeProvider.

					when('/login', {
						template : '<login-panel></login-panel>'
					}).

					when('/viewUrls/:page', {
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

	$rootScope.$on('$locationChangeStart', function (event, next, current) {
		var cookie = $cookies.get('Authentication');
		if (cookie == undefined || cookie == null) {
			$rootScope.$broadcast('logOutEvent', null);
			if($location.url() != "/login" && $location.url() != "/register") {
				$location.path("/login");
			}
		}
	});
});