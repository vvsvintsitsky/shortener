angular.
	module('viewUrls').
		component('viewUrls', {
			templateUrl: './resources/app/view-urls/view-urls.template.html',
			controller: ['$http', '$cookies',
				function ViewUrlsController($http, $cookies) {
					var self = this;
					var url = "http://192.168.100.3:8087/shortener-webapp-1.0.0/service/1";
					var token = $cookies.get('Authentication');
					var header = {
						'Authentication' :  token
					};
					$http.get(url, {
						headers : header
					}).then(function successCallback(response) {
						self.urls = response.data;
					}, function errorCallback(response) {
						alert('error');
					});
				}	
			]
	});