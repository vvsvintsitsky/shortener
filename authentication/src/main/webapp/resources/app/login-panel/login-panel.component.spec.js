describe('loginPanel', function() {

	beforeEach(module('loginPanel'));

	describe('LoginPanelController', function() {

		var $rootScope;
		var $httpBackend;
		var $ctrl;
		var $scope;
		var $location;
		var authenticationService;
		var authHeaderName = 'Authentication';
		var authHeaderValue = 'xxx';
		
		beforeEach(angular.mock.module('ngCookies', 'core.authentication'));
		beforeEach(inject(function(_$rootScope_, $componentController,
				_$httpBackend_, _$location_, _authenticationService_) {
			$httpBackend = _$httpBackend_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$location = _$location_;
			$location.path('/login');
			authenticationService = _authenticationService_;
			$ctrl = $componentController('loginPanel', {
				$rootScope : $rootScope,
				$scope : $scope,
				$location : $location,
				authenticationService : authenticationService
			});
		}));

		afterEach(function() {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});
		
		it('should test login function success', function() {
			var authObject = {
				email : "correctEmail",
				password : "correctPassword"
			}
			var requestHeaders = {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json, text/plain, */*'
			};
			var responseHeaders = {
				'Authentication' : authHeaderValue
			};
			$httpBackend.expectPOST(authenticationService.authPath, authObject,
					requestHeaders).respond(200, '', responseHeaders);
			
			spyOn($rootScope, '$broadcast').and.callThrough();
			$ctrl.email = authObject.email;
			$ctrl.password = authObject.password;
			$ctrl.login();
			
			$httpBackend.flush();
			
			expect($location.path()).toEqual('/viewUrls');
			expect($rootScope.$broadcast).toHaveBeenCalledWith('logInEvent', null);
		});
		
		it('should test login function failure', function() {
			var authObject = {
				email : "correctEmail",
				password : "correctPassword"
			}
			var requestHeaders = {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json, text/plain, */*'
			};
			var errorInfo = {
					url : authenticationService.authPath,
					ex : 'error'
			}
			$httpBackend.expectPOST(authenticationService.authPath, authObject,
					requestHeaders).respond(404, errorInfo);
			
			spyOn($scope, '$broadcast');
			$ctrl.email = authObject.email;
			$ctrl.password = authObject.password;
			$ctrl.login();
			
			$httpBackend.flush();
			
			expect($location.path()).toEqual('/login');
			expect($scope.$broadcast).toHaveBeenCalledWith('failureEvent', errorInfo);
		});

	});

});