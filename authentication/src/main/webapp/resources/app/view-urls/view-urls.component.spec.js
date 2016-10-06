describe('viewUrls', function() {

	beforeEach(module('viewUrls'));

	describe('ViewUrlsController', function() {

		var $componentController;
		var $rootScope;
		var $httpBackend;
		var $ctrl;
		var $scope;
		var $cookies;
		var authHeaderName = 'Authentication';
		var authHeaderValue = 'xxx';
		var urlService;

		beforeEach(angular.mock.module('ngCookies', 'viewUrls'));
		beforeEach(inject(function(_$rootScope_, _$componentController_,
				_$httpBackend_, _$cookies_, _urlService_) {
			$componentController = _$componentController_;
			$httpBackend = _$httpBackend_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$cookies = _$cookies_;
			urlService = _urlService_;
		}));

		afterEach(function() {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		it('should test getting users urls', function() {
			var responseUrls = [ {
				id : 0,
				shortUrl : 'shortUrl',
				longUrl : 'longUrl',
				visited : 0,
				description : 'description',
				accounts : null,
				tags : null
			} ];
			var headers = {
				'Authentication' : $cookies.get(authHeaderName),
				'Accept' : 'application/json, text/plain, */*'
			};
			$cookies.put(authHeaderName, authHeaderValue);

			$httpBackend.expectGET(urlService.servicePath, headers).respond(
					200, responseUrls);

			$ctrl = $componentController('viewUrls', {
				$scope : $scope,
				$cookies : $cookies,
				urlService : urlService
			});

			$httpBackend.flush();

			expect($ctrl.urls).toEqual(responseUrls);
		});

		it('should test adding new url', function() {
			var newUrl = {
				id : 0,
				shortUrl : 'shortUrl',
				longUrl : 'longUrl',
				visited : 0,
				description : 'description',
				accounts : null,
				tags : null
			};
			var headers = {
				'Authentication' : $cookies.get(authHeaderName),
				'Accept' : 'application/json, text/plain, */*'
			};
			$cookies.put(authHeaderName, authHeaderValue);

			$httpBackend.expectGET(urlService.servicePath, headers).respond(
					200, []);

			$ctrl = $componentController('viewUrls', {
				$scope : $scope,
				$cookies : $cookies,
				urlService : urlService
			});
			
			$httpBackend.flush();
			
			expect($ctrl.urls.length).toEqual(0);
			
			$rootScope.$broadcast('urlCreationEvent', newUrl);
			
			expect($ctrl.urls.length).toEqual(1);
		});
	});
});