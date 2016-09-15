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
							function RegistrationController($scope, $location,
									registrationService) {
								var self = this;

								self.register = function() {
									if (this.email != null && this.email != ""
											&& this.password != null
											&& this.password != "") {
										registrationService
												.register(this.email,
														this.password)
												.then(
														function(data) {
															if (data.ex == null) {
																var notification = "Email with activation instructions has been sent to " + self.email;
																alert(notification);
																$location.path("/login");
															} else {
																$scope
																		.$broadcast(
																				'failureEvent',
																				data);
															}
														});
									}

								};
							} ]
				});