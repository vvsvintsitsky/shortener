describe("core.tag", function() {

	beforeEach(module("core.tag"));

	var $httpBackend;
	var tagService;
	var tagName = 'testTag';
	var result;
	
	beforeEach(inject(function(_$httpBackend_, _tagService_) {
		tagService = _tagService_;
		$httpBackend = _$httpBackend_;
		result = null;
	}));

	it('should contain a tagService', function() {
		var serviceCondition = typeof tagService != 'undefined';
		expect(serviceCondition).toBe(true);
	});

	it('should test getUrlsByTag function success', function() {
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
		
		$httpBackend.expectGET(tagService.tagPath + tagName).respond(200,
				responseTag);
		tagService.getTagWithUrls(tagName).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();
		expect(result).toEqual(responseTag);
	});

	it('should test getUrlsByTag function failure', function() {
		var errorInfo = {
			url : tagService.tagPath + tagName,
			ex : "error"
		};
		$httpBackend.expectGET(tagService.tagPath + tagName).respond(404,
				errorInfo);
		tagService.getTagWithUrls(tagName).then(function(returnFromPromise) {
			result = returnFromPromise;
		});
		$httpBackend.flush();
		expect(result.ex).toEqual(errorInfo.ex);
	});

	it('should test addTag function', function() {
		var tags = [];
		var tagDescription = 'tagDescription';
		tagService.addTag(tags, tagDescription);
		
		expect(tags.length).toEqual(1);
	});
	
	it('should test removeTag function', function() {
		var tagDescription = 'tagDescription';
		var tag = {
				id : null,
				description : tagDescription,
				urls : null
		}
		var tags = [tag];
		
		tagService.removeTag(tags, tag);
		
		expect(tags.length).toEqual(0);
	});
	
	afterEach(function() {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});

});