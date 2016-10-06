angular.
	module('core.tag').
		factory('tagService',  ['$http', function($http) {
			var service = {};
			var rootPath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/";
			service.tagPath =  rootPath + "tag/";

			service.getTagWithUrls = function(tagName) {
				var result = $http.get(service.tagPath + tagName).then(function successCallback(response) {
					return response.data;
				}, function errorCallback(response) {
					return response.data;
				});
				return result;
			}
			
			service.removeTag = function(tags, tag) {
				var index = tags.indexOf(tag);
				tags.splice(index, 1);
			}
			
			service.addTag = function(tags, tgDscrptn) {
				var tg = {
						id: null,
						description: tgDscrptn,
						urls: null
					};
				tags.push(tg);
			}
			
			return service;
		}
	]);