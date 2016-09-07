angular.
	module('login').
		component('login', {
			templateUrl: './resources/app/login/login.template.html',
			controller: ['$http', '$cookies',
				function LoginController($http, $cookies) {
					this.login = function() {
						var url = "http://192.168.100.3:8087/shortener-webapp-1.0.0/auth/";
						var header = {
							'Content-Type' : 'application/json'
						};
						var accountWeb = {
							email : this.email,
							password : this.password
						}
						$http.post(url, accountWeb, {
							headers : header
						}).then(function successCallback(response) {
							$cookies.put('Authentication', response.headers('Authentication'));
						}, function errorCallback(response) {
							alert('error');
						});
					}
				}
			]
		});