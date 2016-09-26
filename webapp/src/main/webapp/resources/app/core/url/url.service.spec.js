describe("core.url", function() {

	beforeEach(module("core.url"));

	var $httpBackend;
	var urlService;
	var result;
	var authHeaderName = 'Authentication';
	var authHeaderValue = 'xxx';

	beforeEach(angular.mock.module('ngCookies', 'core.url'));
	beforeEach(inject(function(_$httpBackend_, _urlService_, $cookies) {
		urlService = _urlService_;
		$httpBackend = _$httpBackend_;
		$cookies.remove(authHeaderName);
		result = null;
	}));
	
	afterEach(function() {
	     $httpBackend.verifyNoOutstandingExpectation();
	     $httpBackend.verifyNoOutstandingRequest();
	   });

	it('should contain an urlService', function() {
		var serviceCondition = typeof urlService != 'undefined';
		expect(serviceCondition).toBe(true);
	});

	it('should test getInfo function success', function() {
		var responseTags = [ {
			id : 0,
			description : 'description0',
			urls : null
		}, {
			id : 1,
			description : 'description1',
			urls : null
		} ];
		var short = 'shortUrl';
		var responseUrl = {
			id : 0,
			shortUrl : short,
			longUrl : 'longUrl',
			visited : 0,
			description : 'description',
			accounts : null,
			tags : responseTags
		};
		$httpBackend.expectGET(urlService.infoPath + short).respond(200,
				responseUrl);
		urlService.getInfo(short).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();
		expect(result).toEqual(responseUrl);
	});

	it('should test getInfo function failure', function() {

		var short = 'shortUrl';
		var errorInfo = {
			url : urlService.infoPath + short,
			ex : 'error'
		};
		$httpBackend.expectGET(urlService.infoPath + short).respond(404,
				errorInfo);
		urlService.getInfo(short).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();
		expect(result).toEqual(errorInfo);
	});

	it('should test getUsersUrls function success', inject(function($cookies) {

		var responseUrls = [ {
			id : 0,
			shortUrl : 'shortUrl',
			longUrl : 'longUrl',
			visited : 0,
			description : 'description',
			accounts : null,
			tags : null
		} ];

		expect($cookies.get(authHeaderName)).toBe(undefined);

		$cookies.put(authHeaderName, authHeaderValue);
		var headers = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*'
		};
		$httpBackend.expectGET(urlService.servicePath, headers).respond(200,
				responseUrls);
		urlService.getUsersUrls().then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();
		expect(result).toEqual(responseUrls);
	}));

	it('should test getUsersUrls function failure', inject(function($cookies) {

		var errorInfo = {
			url : urlService.servicePath,
			ex : 'error'
		};

		expect($cookies.get(authHeaderName)).toBe(undefined);

		$cookies.put(authHeaderName, authHeaderValue);
		var headers = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*'
		};
		$httpBackend.expectGET(urlService.servicePath, headers).respond(404,
				errorInfo);
		urlService.getUsersUrls().then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();
		expect(result).toEqual(false);
	}));
	
	it('should test updateUrl function success', inject(function($cookies) {

		var requestTags = [ {
			id : null,
			description : 'description0',
			urls : null
		}, {
			id : null,
			description : 'description1',
			urls : null
		} ];
		var requestUrl = {
			id : null,
			shortUrl : 'shortUrl',
			longUrl : null,
			visited : null,
			description : null,
			accounts : null,
			tags : requestTags
		};
		var resposeData = {
				info : 'info'
		};
		
		expect($cookies.get(authHeaderName)).toBe(undefined);

		$cookies.put(authHeaderName, authHeaderValue);
		var headers = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*',
			'Content-Type' : 'application/json;charset=utf-8'
		};
		$httpBackend.expectPUT(urlService.servicePath, requestUrl, headers).respond(200,
				responseData);
		urlService.updateUrl(requestUrl).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		
		$httpBackend.flush();
		expect(result).toEqual(responseData);
	}));
	
	it('should test updateUrl function failure', inject(function($cookies) {

		var requestTags = [ {
			id : null,
			description : 'description0',
			urls : null
		}, {
			id : null,
			description : 'description1',
			urls : null
		} ];
		var requestUrl = {
			id : null,
			shortUrl : 'shortUrl',
			longUrl : null,
			visited : null,
			description : null,
			accounts : null,
			tags : requestTags
		};
		var errorInfo = {
				url : urlService.servicePath,
				ex : 'error'
		};
		expect($cookies.get(authHeaderName)).toBe(undefined);

		$cookies.put(authHeaderName, authHeaderValue);
		var headers = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*',
			'Content-Type' : 'application/json;charset=utf-8'
		};
		$httpBackend.expectPUT(urlService.servicePath, requestUrl, headers).respond(404,
				errorInfo);
		urlService.updateUrl(requestUrl).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		
		$httpBackend.flush();
		expect(result).toEqual(errorInfo);
	}));
	
	it('should test createUrl function success', inject(function($cookies) {

		var requestTags = [ {
			id : null,
			description : 'description0',
			urls : null
		}, {
			id : null,
			description : 'description1',
			urls : null
		} ];
		var description = 'description';
		var longUrl = 'longUrl';
		var requestUrl = {
				id: null,
				account: null,
				shortUrl: null,
				visited: null,
				description: description,
				longUrl: longUrl,
				tags: requestTags
		}
		expect($cookies.get(authHeaderName)).toBe(undefined);

		$cookies.put(authHeaderName, authHeaderValue);
		var headers = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*',
			'Content-Type' : 'application/json;charset=utf-8'
		};
		$httpBackend.expectPOST(urlService.servicePath, requestUrl, headers).respond(200,
				requestUrl);
		urlService.createUrl(longUrl, description, requestTags).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		
		$httpBackend.flush();
		expect(result).toEqual(requestUrl);
	}));
	
	it('should test createUrl function failure', inject(function($cookies) {

		var requestTags = [ {
			id : null,
			description : 'description0',
			urls : null
		}, {
			id : null,
			description : 'description1',
			urls : null
		} ];
		var description = 'description';
		var longUrl = 'longUrl';
		var requestUrl = {
				id: null,
				account: null,
				shortUrl: null,
				visited: null,
				description: description,
				longUrl: longUrl,
				tags: requestTags
		}
		expect($cookies.get(authHeaderName)).toBe(undefined);

		$cookies.put(authHeaderName, authHeaderValue);
		var headers = {
			'Authentication' : $cookies.get(authHeaderName),
			'Accept' : 'application/json, text/plain, */*',
			'Content-Type' : 'application/json;charset=utf-8'
		};
		$httpBackend.expectPOST(urlService.servicePath, requestUrl, headers).respond(404,
				'');
		urlService.createUrl(longUrl, description, requestTags).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		
		$httpBackend.flush();
		expect(result).toEqual('');
	}));
});