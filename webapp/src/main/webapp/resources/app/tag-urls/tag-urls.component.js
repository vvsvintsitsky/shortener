angular.
	module('tagUrls').
		component('tagUrls', {
			templateUrl: './resources/app/tag-urls/tag-urls.template.html',
			controller: ['$scope', '$http', '$routeParams', 'tagService',
				function TagUrlsController($scope, $http, $routeParams, tagService) {
					var self = this;
					
					tagService.getUrlsByTag($routeParams.tagDescription).then(function(data) {
						if(data.ex == null) {
							self.tag = data;
						} else {
							$scope.$broadcast('failureEvent', data);
						}
					});
				}
			]
		});