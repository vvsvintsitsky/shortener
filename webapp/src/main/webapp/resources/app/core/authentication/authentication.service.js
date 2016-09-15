angular.
	module('core.authentication').
		factory('authenticationService',  ['$http', '$cookies', function($http, $cookies) {
			var service = {};
			var rootPath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/";
			var authPath = rootPath + "auth/";
			var servicePath = rootPath + "service/";
			var header = {
					'Content-Type' : 'application/json'
			};
			
			service.login = function(email, password) {
				var accountWeb = {
						email : email,
						password : password
				}
				var result = $http.post(authPath, accountWeb, {
					headers : header
				}).then(function successCallback(response) {
					$cookies.put('Authentication', response.headers('Authentication'));
					return true;
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			
			return service;
		}
	]);