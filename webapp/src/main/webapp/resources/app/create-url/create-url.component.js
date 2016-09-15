angular
		.module('createUrl')
		.component(
				'createUrl',
				{
					templateUrl : './resources/app/create-url/create-url.template.html',
					controller : [
							'$scope',
							'urlService',
							'tagService',
							function CreateUrlController($scope, urlService,
									tagService) {
								var self = this;
								self.tags = [];

								self.createUrl = function() {
									if (creationCondition()) {
										urlService
												.createUrl(self.longUrl,
														self.description,
														self.tags)
												.then(
														function(data) {
															if (data.ex == null) {
																$scope
																		.$emit(
																				'urlCreationEvent',
																				data);
															} else {
																$scope.$parent
																		.$broadcast(
																				'failureEvent',
																				data);
															}
														});
									}
								};

								self.addTag = function(tgDscrptn) {
									if (tgDscrptn != null && tgDscrptn != "") {
										tagService.addTag(self.tags, tgDscrptn);
									}
									$scope.tgDscrptn = null;
								};

								self.removeTag = function(tag) {
									tagService.removeTag(self.tags, tag);
								};

								function creationCondition() {
									return (self.tags.length != 0)
											&& (self.longUrl != null)
											&& (self.longUrl != "")
											&& (self.description != null)
											&& (self.description != "");
								}
							} ]
				});