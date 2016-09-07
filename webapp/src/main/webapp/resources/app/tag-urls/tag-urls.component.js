angular.
	module('tagUrls').
		component('tagUrls', {
			templateUrl: './resources/app/tag-urls/tag-urls.template.html',
			controller: ['$http', '$routeParams',
				function TagUrlsController($http, $routeParams) {
					var self = this;
					var location = "http://192.168.100.3:8087/shortener-webapp-1.0.0/tag/" + $routeParams.tagDescription;
					$http.get(location).then(function successCallback(response) {
						self.tag = response.data;
					}, function errorCallback(response) {
						alert('error');
					});
				}
			]
		});