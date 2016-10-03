angular.
	module('core.url').
		factory('urlService',  ['$http', '$cookies', function($http, $cookies) {
			var service = {};
			var rootPath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/";
			
			function generateAuthenticationHeader() {
				var token = $cookies.get('Authentication');
				var header = {
					'Authentication' :  token
				};
				return header;
			}
			
			service.servicePath =  rootPath + "service/";
			service.infoPath = rootPath + "info/";
			service.accountUrlsPath = service.servicePath + "urls/page/";
			
			service.getInfo = function(shortUrl) {
				var result =
				$http.get(service.infoPath + shortUrl).then(function successCallback(response) {
					return response.data;
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			
			service.getUsersUrls = function(page) {
				var token = $cookies.get('Authentication');
				var header = {
					'Authentication' :  token
				};
				if(page == undefined) {
					page = 0;
				}
				var result =
					$http.get(service.accountUrlsPath + page, {
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
				var result = $http.put(service.servicePath, url, {
					headers : header
				}).then(function successCallback(response) {
					return response.data;
				}, function errorCallback(response) {
					return response.data;
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
				var result = $http.post(service.servicePath, url, {
					headers : header
				}).then(function successCallback(response) {
					return response.data
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			return service;
		}
	]);