angular.
	module('url').
		component('url', {
			templateUrl: './resources/app/url/url.template.html',
			controller: ['$scope', '$http', '$routeParams', '$cookies',
				function UrlController($scope, $http, $routeParams, $cookies) {
					var self = this;
					var infoPath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/info/" + $routeParams.urlToView;
					var updatePath = "http://192.168.100.3:8087/shortener-webapp-1.0.0/service/";
					$http.get(infoPath).then(function successCallback(response) {
						self.url = response.data;
					}, function errorCallback(response) {
						alert('error');
					});
					
					self.shout = function(tag) {
						var i = self.url.tags.length;
						var index = self.url.tags.indexOf(tag);
						self.url.tags.splice(index, 1);
						var j = self.url.tags.length;
						//alert(i + ' ' + j);
					}
					
					self.addTag = function(tgDscrptn) {
						var tg = {
							id: null,
							description: tgDscrptn,
							urls: null
						};
						self.url.tags.push(tg);
						$scope.tgDscrptn = null;
					}
					
					self.updateUrl = function() {
						var token = $cookies.get('Authentication');
						var header = {
							'Authentication' :  token
						};
						$http.put(updatePath, self.url, {
							headers : header
						}).then(function successCallback(response) {
							alert("OK");
						}, function errorCallback(response) {
							alert('error');
						});
					}
				}
			]
		});