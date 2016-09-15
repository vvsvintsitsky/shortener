angular.
	module('viewUrls').
		component('viewUrls', {
			templateUrl: './resources/app/view-urls/view-urls.template.html',
			controller: ['$scope', '$http', '$cookies', 'urlService',
				function ViewUrlsController($scope, $http, $cookies, urlService) {
					var self = this;
					$scope.$on('urlCreationEvent', function(event, data) {
						self.urls.push(data);
					});
					urlService.getUsersUrls().then(function(data) {
						self.urls = data;
					});
				}	
			]
	});