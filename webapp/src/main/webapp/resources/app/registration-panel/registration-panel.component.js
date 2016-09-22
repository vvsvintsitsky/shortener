angular
		.module('registrationPanel')
		.component(
				'registrationPanel',
				{
					templateUrl : './resources/app/registration-panel/registration-panel.template.html',
					controller : [
							'$scope',
							'$location',
							'registrationService',
							function RegistrationPanelController($scope,
									$location, registrationService) {
								var self = this;

								self.register = function() {
									if (self.validateCredentials()) {
										registrationService
												.register(this.email,
														this.password)
												.then(
														function(data) {
															if (data.ex == null) {
																var notification = data
																		+ self.email;
																alert(notification);
																$location
																		.path("/login");
															} else {
																$scope
																		.$broadcast(
																				'failureEvent',
																				data);
															}
														});
									}

								};

								self.validateCredentials = function() {
									return self.email != null
											&& self.email != ""
											&& self.password != null
											&& self.password != "";
								}
							} ]
				});