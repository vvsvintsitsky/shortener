angular.
	module('core.registration').
		factory('registrationService',  ['$http', function($http) {
			var service = {};
			var rootPath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/";
			var registrationPath = rootPath + "register/";
			var header = {
					'Content-Type' : 'application/json'
			};
			
			service.register = function(email, password) {
				var accountWeb = {
						email : email,
						password : password
				}
				var result = $http.post(registrationPath, accountWeb, {
					headers : header
				}).then(function successCallback(response) {
					return response.data;
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			
			return service;
		}
	]);