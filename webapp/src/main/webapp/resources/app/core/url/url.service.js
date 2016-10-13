angular.
	module('core.url').
		factory('urlService',  ['$http', '$cookies', function($http, $cookies) {
			var service = {};
			var rootPath = "http://localhost:8087/shortener-webapp-1.0.0/";
			
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
				if(page == undefined) {
					page = 0;
				}
				var result =
					$http.get(service.accountUrlsPath + page).then(function successCallback(response) {
						return response.data;
					}, function errorCallback(response) {
						return false;
					});
				return result;
			}
			
			service.updateUrl = function(url) {
				var result = $http.put(service.servicePath, url).then(function successCallback(response) {
					return response.data;
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			
			service.createUrl = function(lngUrl, dscrptn, tgs) {
				var url = {
						id: null,
						account: null,
						shortUrl: null,
						visited: null,
						description: dscrptn,
						longUrl: lngUrl,
						tags: tgs
				}
				var result = $http.post(service.servicePath, url).then(function successCallback(response) {
					return response.data
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			return service;
		}
	]);