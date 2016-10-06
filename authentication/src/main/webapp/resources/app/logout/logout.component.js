angular.
	module('logout').
		component('logout', {
			templateUrl: './resources/app/logout/logout.template.html',
			controller: ['$rootScope', '$cookies', '$location',
				function LogoutController($rootScope, $cookies, $location) {
					var self = this;
					$cookies.remove('Authentication');
					$location.path('/login');
					$rootScope.$broadcast('logOutEvent', null);
				}
			]
		});