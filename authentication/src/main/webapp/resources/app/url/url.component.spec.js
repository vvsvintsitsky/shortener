describe('url', function() {

	beforeEach(module('url'));

	describe('UrlController', function() {

		var $componentController;
		var $rootScope;
		var $httpBackend;
		var $ctrl;
		var $scope;
		var $cookies;
		var $routeParams;
		var authHeaderName = 'Authentication';
		var authHeaderValue = 'xxx';
		var urlService;
		var tagService;
		var authenticationService;

		beforeEach(angular.mock.module('ngCookies', 'url'));
		beforeEach(angular.mock.module('ngRoute', 'url'));
		beforeEach(inject(function(_$rootScope_, _$componentController_,
				_$httpBackend_, _$cookies_, _$routeParams_, _urlService_,
				_tagService_, _authenticationService_) {
			$componentController = _$componentController_;
			$httpBackend = _$httpBackend_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$cookies = _$cookies_;
			$routeParams = _$routeParams_;
			urlService = _urlService_;
			tagService = _tagService_;
			authenticationService = _authenticationService_;
		}));

		afterEach(function() {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		it('should test getting url info', function() {
			var description = 'description';
			var longUrl = 'longUrl';
			var shortUrl = 'shortUrl';
			var visited = 0;
			var responseTag = {
				id : null,
				description : 'description0',
				urls : null
			};
			var responseUrl = {
				id : null,
				account : null,
				shortUrl : shortUrl,
				visited : visited,
				description : description,
				longUrl : longUrl,
				tags : [ responseTag ]
			};

			$routeParams = {
				urlToView : responseUrl.shortUrl
			};

			$httpBackend.expectGET(authenticationService.ownershipPath + responseUrl.shortUrl)
					.respond(200, true);

			$httpBackend.expectGET(urlService.infoPath + responseUrl.shortUrl)
					.respond(200, responseUrl);

			$ctrl = $componentController('url', {
				$scope : $scope,
				$routeParams : $routeParams,
				urlService : urlService,
				tagService : tagService,
				authenticationService : authenticationService
			});

			$httpBackend.flush();

			expect($ctrl.ownership).toEqual(true);
			expect($ctrl.url).toEqual(responseUrl);
		});

	});

});