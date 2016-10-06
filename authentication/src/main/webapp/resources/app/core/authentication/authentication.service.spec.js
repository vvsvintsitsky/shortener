describe("core.authentication", function() {

	beforeEach(module("core.authentication"));

	var $httpBackend;
	var $cookies;
	var authenticationService;
	var authHeaderName = 'Authentication';
	var authHeaderValue = 'xxx';
	var result;
	
	beforeEach(angular.mock.module('ngCookies', 'core.authentication'));
	beforeEach(inject(function(_$httpBackend_, _authenticationService_,
			_$cookies_) {
		$cookies = _$cookies_;
		$cookies.remove(authHeaderName);
		authenticationService = _authenticationService_;
		$httpBackend = _$httpBackend_;
	}));

	afterEach(function() {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});
	
	it('should contain an authenticationService', function() {
		expect(authenticationService).not.toBe(undefined);
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
		authenticationService.login('correctEmail', 'correctPassword').then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();

		expect($cookies.get(authHeaderName)).toBe(authHeaderValue);
		expect(result).toBe(true);
	});

	it('should test login function 401 failure', function() {
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
				requestHeaders).respond(401, errorInfo);
		authenticationService.login('correctEmail', 'correctPassword').then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();

		expect($cookies.get(authHeaderName)).toBe(undefined);
		expect(result).toEqual(errorInfo);
	});
	
	it('should test checkOwnership function success', function() {

		$cookies.put(authHeaderName, authHeaderValue);
		var shortUrl = 'shortUrl';
		var requestHeaders = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*'
		};
		$httpBackend.expectGET(authenticationService.ownershipPath + shortUrl,
				requestHeaders).respond(200, true);
			
		authenticationService.checkOwnership(shortUrl).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();

		expect(result).toBe(true);
	});
	
	it('should test checkOwnership function failure', function() {

		$cookies.put(authHeaderName, authHeaderValue);
		var shortUrl = 'shortUrl';
		var requestHeaders = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*'
		};
		var responseData = 'data';
		$httpBackend.expectGET(authenticationService.ownershipPath + shortUrl,
				requestHeaders).respond(404, responseData);
			
		authenticationService.checkOwnership(shortUrl).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();

		expect(result).toEqual(responseData);
	});

	afterEach(function() {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});
});