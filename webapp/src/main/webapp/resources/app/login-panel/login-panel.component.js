angular
		.module('loginPanel')
		.component(
				'loginPanel',
				{
					templateUrl : './resources/app/login-panel/login-panel.template.html',
					controller : [
							'$rootScope',
							'$scope',
							'$location',
							'authenticationService',
							function LoginPanelController($rootScope, $scope, $location,
									authenticationService) {
								this.login = function() {
									var result = authenticationService
											.login(this.email, this.password)
											.then(
													function(data) {
														if (data === true) {
															$location
																	.path('/viewUrls/0');
															$rootScope.$broadcast('logInEvent', null);
														} else {
															$scope.$broadcast('failureEvent', data);
														}
													});
								}
							} ]
				});