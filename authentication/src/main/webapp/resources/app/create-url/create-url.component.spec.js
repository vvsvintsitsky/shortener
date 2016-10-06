describe('createUrl', function() {

	beforeEach(module('createUrl'));

	describe('CreateUrlController', function() {

		var $rootScope;
		var $httpBackend;
		var $ctrl;
		var $scope;
		var $cookies;
		var authHeaderName = 'Authentication';
		var authHeaderValue = 'xxx';
		var urlService;
		var tagService;

		beforeEach(angular.mock.module('ngCookies', 'createUrl'));
		beforeEach(inject(function(_$rootScope_, $componentController,
				_$httpBackend_, _$cookies_, _urlService_, _tagService_) {
			$httpBackend = _$httpBackend_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$cookies = _$cookies_;
			urlService = _urlService_;
			tagService = _tagService_;
			$ctrl = $componentController('createUrl', {
				$scope : $scope,
				urlService : urlService,
				tagService : tagService
			});
		}));

		afterEach(function() {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		it('should test urlCreationCondition function', function() {
			expect($ctrl.urlCreationCondition()).toEqual(false);
			$ctrl.tags = [ {
				id : null,
				description : 'description',
				urls : null
			} ];
			expect($ctrl.urlCreationCondition()).toEqual(false);
			$ctrl.longUrl = 'longUrl';
			expect($ctrl.urlCreationCondition()).toEqual(false);
			$ctrl.description = 'description';
			expect($ctrl.urlCreationCondition()).toEqual(true);
		});

		it('should test addTag function', function() {
			$scope.tgDscrptn = 'a';
			expect($ctrl.tags).toEqual([]);
			$ctrl.addTag();
			expect($ctrl.tags.length).toEqual(0);
			expect($scope.tgDscrptn).toEqual('a');

			$ctrl.addTag('');
			expect($ctrl.tags.length).toEqual(0);
			expect($scope.tgDscrptn).toEqual('a');

			$ctrl.addTag('a');
			expect($ctrl.tags.length).toEqual(1);
			expect($scope.tgDscrptn).toEqual(null);
		});

		it('should test removeTag function', function() {
			var tag = {
				id : null,
				description : 'description',
				urls : null
			};
			$ctrl.tags = [ tag ];
			$ctrl.removeTag(tag);
			expect($ctrl.tags.length).toEqual(0);
		});

		it('should test createUrl function', function() {
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
				id : null,
				account : null,
				shortUrl : null,
				visited : null,
				description : description,
				longUrl : longUrl,
				tags : requestTags
			}
			var requestHeaders = {
				'Authentication' : $cookies.get(authHeaderName),
				'Accept' : 'application/json, text/plain, */*',
				'Content-Type' : 'application/json;charset=utf-8'
			};
			var errorInfo = {
				url : urlService.servicePath,
				ex : 'error'
			}

			$ctrl.tags = requestTags;
			$ctrl.longUrl = longUrl;
			$ctrl.description = description;
			$cookies.put(authHeaderName, authHeaderValue);

			$httpBackend.expectPOST(urlService.servicePath, requestUrl,
					requestHeaders).respond(200, requestUrl);
			spyOn($scope, '$emit');
			$ctrl.createUrl();

			$httpBackend.flush();
			expect($scope.$emit).toHaveBeenCalledWith('urlCreationEvent',
					requestUrl);

			$httpBackend.expectPOST(urlService.servicePath, requestUrl,
					requestHeaders).respond(400, errorInfo);
			spyOn($scope.$parent, '$broadcast');
			$ctrl.createUrl();

			$httpBackend.flush();
			expect($scope.$parent.$broadcast).toHaveBeenCalledWith('failureEvent',
					errorInfo);
		});

	});

});