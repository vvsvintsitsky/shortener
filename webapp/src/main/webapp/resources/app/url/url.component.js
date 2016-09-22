angular.module('url').component(
		'url',
		{
			templateUrl : './resources/app/url/url.template.html',
			controller : [
					'$scope',
					'$routeParams',
					'urlService',
					'tagService',
					'authenticationService',
					function UrlController($scope, $routeParams, urlService,
							tagService, authenticationService) {
						var self = this;
						authenticationService.checkOwnership(
								$routeParams.urlToView).then(function(data) {
							self.ownership = data;
						});

						urlService.getInfo($routeParams.urlToView)
								.then(
										function(data) {
											if (data.ex == null) {
												self.url = data;
											} else {
												$scope.$broadcast(
														'failureEvent', data);
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

						self.updateCondition = function() {
							return self.url.tags.length != 0;
						}
					} ]
		});