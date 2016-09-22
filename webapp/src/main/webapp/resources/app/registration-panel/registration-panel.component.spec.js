describe('registrationPanel', function() {

	beforeEach(module('registrationPanel'));

	describe('RegistrationPanelController', function() {

		var $rootScope;
		var $httpBackend;
		var $ctrl;
		var $scope;
		var $location;
		var registrationService;
		var result;
		
		beforeEach(inject(function(_$rootScope_, $componentController,
				_$httpBackend_, _$location_, _registrationService_) {
			$httpBackend = _$httpBackend_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$location = _$location_;
			registrationService = _registrationService_;
			$ctrl = $componentController('registrationPanel', {
				$scope : $scope,
				$location : $location,
				registrationService : registrationService
			})
		}));

		afterEach(function() {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		it('should test validateCredentials function', function() {
			var email = 'email';
			var password = 'password';
			expect($ctrl.validateCredentials()).toEqual(false);
			$ctrl.password = password;
			expect($ctrl.validateCredentials()).toEqual(false);
			$ctrl.password = undefined;
			$ctrl.email = email;
			expect($ctrl.validateCredentials()).toEqual(false);
			$ctrl.password = password;
			$ctrl.email = email;
			expect($ctrl.validateCredentials()).toEqual(true);
		});

		it('should test register function 200', function() {
			var email = 'email';
			var password = 'password';
			var accountWeb = {
					email : email,
					password : password
			}
			var responseData = 'data + ';
			$ctrl.password = password;
			$ctrl.email = email;
			
			$httpBackend.expectPOST(registrationService.registrationPath,
					accountWeb).respond(200, responseData);
			
			$ctrl.register();
			
			$httpBackend.flush();
			
			expect($location.path()).toEqual('/login');
		});
		
		it('should test register function 400', function() {
			var email = 'email';
			var password = 'password';
			var accountWeb = {
					email : email,
					password : password
			}
			var errorInfo = {
					url : registrationService.registrationPath,
					ex : 'error'
			}
			$ctrl.password = password;
			$ctrl.email = email;
			
			$httpBackend.expectPOST(registrationService.registrationPath,
					accountWeb).respond(400, errorInfo);
			spyOn($scope, '$broadcast');
			
			$ctrl.register();
			
			$httpBackend.flush();
			
			expect($scope.$broadcast).toHaveBeenCalledWith('failureEvent', errorInfo);
		});
	});

});