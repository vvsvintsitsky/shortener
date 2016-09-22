describe('tagUrls', function() {

	beforeEach(module('tagUrls'));

	describe('TagUrlsController', function() {

		var $componentController;
		var $httpBackend;
		var $ctrl;
		var $rootScope;
		var $scope;
		var $routeParams;
		var tagService;

		beforeEach(angular.mock.module('ngRoute', 'tagUrls'));
		beforeEach(inject(function(_$componentController_, _$httpBackend_, _$rootScope_,
				_$routeParams_, _tagService_) {
			$componentController = _$componentController_;
			$httpBackend = _$httpBackend_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$routeParams = _$routeParams_;
			tagService = _tagService_;
		}));

		afterEach(function() {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		it('should test getting urls by tag success', function() {
			var description = 'description';
			var longUrl = 'longUrl';
			var shortUrl = 'shortUrl';
			var visited = 0;
			var responseUrls = [{
				id : null,
				account : null,
				shortUrl : shortUrl,
				visited : visited,
				description : description,
				longUrl : longUrl,
				tags : null
			}];
			var responseTag = {
				id : null,
				description : 'description0',
				urls : responseUrls
			};
			$routeParams = {
				tagDescription : responseTag.description
			};
			
			$httpBackend.expectGET(tagService.tagPath + responseTag.description).respond(200,
					responseTag);
			
			$ctrl = $componentController('tagUrls', {
				$scope : $scope,
				$routeParams : $routeParams,
				tagService : tagService
			});
			
			$httpBackend.flush();
			
			expect($ctrl.tag).toEqual(responseTag);
		});

		it('should test getting urls by tag failure', function() {
			var tagName = 'tagName';
			var errorInfo = {
					url : tagService.tagPath,
					ex : 'error'
			}
			$routeParams = {
				tagDescription : tagName
			};
			
			$httpBackend.expectGET(tagService.tagPath + tagName).respond(404,
					errorInfo);
			
			spyOn($scope, '$broadcast');
			
			$ctrl = $componentController('tagUrls', {
				$scope : $scope,
				$routeParams : $routeParams,
				tagService : tagService
			});
			
			$httpBackend.flush();
			
			expect($scope.$broadcast).toHaveBeenCalledWith('failureEvent', errorInfo);
		});
		
	});

});