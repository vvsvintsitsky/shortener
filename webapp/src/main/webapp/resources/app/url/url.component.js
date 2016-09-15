angular.module('url').component(
		'url',
		{
			templateUrl : './resources/app/url/url.template.html',
			controller : [
					'$scope',
					'$cookies',
					'$routeParams',
					'urlService',
					'tagService',
					function UrlController($scope, $cookies, $routeParams, urlService,
							tagService) {
						var self = this;
						self.isLoggedIn = $cookies.get('Authentication');

						urlService.getInfo($routeParams.urlToView).then(
								function(data) {
									if(data.ex == null) {
										self.url = data;
									} else {
										$scope.$broadcast('failureEvent', data);
									}
								});

						self.removeTag = function(tag) {
							tagService.removeTag(self.url.tags, tag);
						}

						self.addTag = function(tgDscrptn) {
							tagService.addTag(self.url.tags, tgDscrptn);
							$scope.tgDscrptn = null;
						}

						self.updateUrl = function() {
							if (updateCondition()) {
								urlService.updateUrl(self.url).then(
										function(data) {
										});
							}
						}

						function updateCondition() {
							return self.url.tags.length != 0;
						}
					} ]
		});