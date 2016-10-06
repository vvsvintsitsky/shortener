describe("core.registration", function() {

	beforeEach(module("core.registration"));

	var $httpBackend;
	var registrationService;
	var result;

	beforeEach(inject(function(_$httpBackend_, _registrationService_) {
		registrationService = _registrationService_;
		$httpBackend = _$httpBackend_;
		result = null;
	}));

	it('should contain a registrationService', function() {
		var serviceCondition = typeof registrationService != 'undefined';
		expect(serviceCondition).toBe(true);
	});

	it('should test register function success', function() {
		var accountWeb = {
			email : 'email',
			password : 'password'
		}
		var responseData = 'ok';
		$httpBackend.expectPOST(registrationService.registrationPath,
				accountWeb).respond(200, responseData);
		registrationService.register(accountWeb.email, accountWeb.password)
				.then(function(returnFromPromise) {
					result = returnFromPromise;
				});
		$httpBackend.flush();
		expect(result).toEqual(responseData);
	});

	it('should test register function failure', function() {
		var accountWeb = {
			email : 'email',
			password : 'password'
		}
		var errorInfo = {
				url : registrationService.registrationPath,
				ex : 'error'
		};
		$httpBackend.expectPOST(registrationService.registrationPath,
				accountWeb).respond(404, errorInfo);

		registrationService.register(accountWeb.email, accountWeb.password)
				.then(function(returnFromPromise) {
					result = returnFromPromise;
				});
		$httpBackend.flush();
		
		expect(result).toEqual(errorInfo);
	});

});