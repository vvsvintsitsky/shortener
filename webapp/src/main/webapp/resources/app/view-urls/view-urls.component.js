angular.
	module('viewUrls').
		component('viewUrls', {
			templateUrl: './resources/app/view-urls/view-urls.template.html',
			controller: ['$scope', '$http', '$cookies', '$routeParams', 'urlService',
				function ViewUrlsController($scope, $http, $cookies, $routeParams, urlService) {
					var self = this;
					self.urls = [];
					$scope.$on('urlCreationEvent', function(event, data) {
						self.urls.push(data);
					});
					urlService.getUsersUrls($routeParams.page).then(function(data) {
						self.urls = data;
					});
				}	
			]
	});