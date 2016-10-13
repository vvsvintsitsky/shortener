angular
		.module('core.authentication')
		.factory(
				'authenticationService',
				[
						'$http',
						'$cookies',
						function($http, $cookies) {
							var service = {};
							var rootPath = "http://localhost:8087/shortener-webapp-1.0.0/";
							service.authPath = rootPath + "auth/";
							service.servicePath = rootPath + "service/"
							service.ownershipPath = service.servicePath
									+ "ownership/"
							var header = {
								'Content-Type' : 'application/json'
							};

							service.login = function(email, password) {
								var accountWeb = {
									email : email,
									password : password
								}
								var result = $http
										.post(service.authPath, accountWeb, {
											headers : header
										})
										.then(
												function successCallback(
														response) {
													return true;
												},
												function errorCallback(response) {
													return response.data;
												});
								return result;
							}

							service.checkOwnership = function(shortUrl) {
								if ($cookies.get('Authentication') == null) {
									return false;
								}
								var result = $http.get(
										service.ownershipPath + shortUrl).then(
										function successCallback(response) {
											return response.data;
										}, function errorCallback(response) {
											return response.data;
										});
								return result;
							}
							
							service.logout = function() {
								$cookies.remove('Authentication');
							}
							
							return service;
						} ]);