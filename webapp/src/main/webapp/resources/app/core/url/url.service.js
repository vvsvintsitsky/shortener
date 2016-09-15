angular.
	module('core.url').
		factory('urlService',  ['$http', '$cookies', function($http, $cookies) {
			var service = {};
			var rootPath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/";
			var servicePath =  rootPath + "service/";
			var infoPath = rootPath + "info/";
			
			service.getInfo = function(shortUrl) {
				var result =
				$http.get(infoPath + shortUrl).then(function successCallback(response) {
					return response.data;
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			
			service.getUsersUrls = function() {
				var token = $cookies.get('Authentication');
				var header = {
					'Authentication' :  token
				};
				var result =
					$http.get(servicePath, {
						headers : header
					}).then(function successCallback(response) {
						return response.data;
					}, function errorCallback(response) {
						return false;
					});
				return result;
			}
			
			service.updateUrl = function(url) {
				var token = $cookies.get('Authentication');
				var header = {
					'Authentication' :  token
				};
				var result = $http.put(servicePath, url, {
					headers : header
				}).then(function successCallback(response) {
					return true;
				}, function errorCallback(response) {
					return false;
				});
				return result;
			}
			
			service.createUrl = function(lngUrl, dscrptn, tgs) {
				var token = $cookies.get('Authentication');
				var header = {
					'Authentication' :  token
				};
				var url = {
						id: null,
						account: null,
						shortUrl: null,
						visited: null,
						description: dscrptn,
						longUrl: lngUrl,
						tags: tgs
				}
				var result = $http.post(servicePath, url, {
					headers : header
				}).then(function successCallback(response) {
					return response.data
				}, function errorCallback(response) {
					return false;
				});
				return result;
			}
			return service;
		}
	]);