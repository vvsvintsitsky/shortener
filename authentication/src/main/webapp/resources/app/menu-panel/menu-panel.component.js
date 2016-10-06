angular.
	module('menuPanel').
		component('menuPanel', {
			templateUrl: './resources/app/menu-panel/menu-panel.template.html',
			controller: ['$scope', '$cookies',
				function MenuPanelController($scope, $cookies) {
					var self = this;
					self.isLoggedIn = $cookies.get('Authentication') != undefined;
					
					$scope.$on('logInEvent', function(event, data) {
						self.isLoggedIn = true;
					});
					
					$scope.$on('logOutEvent', function(event, data) {
						self.isLoggedIn = false;
					});
				}
			]
		});