describe('logout', function() {

	beforeEach(module('logout'));

	describe('LogoutController', function() {

		var $rootScope;
		var $location;
		var $cookies;
		var authHeaderName = 'Authentication';
		var authHeaderValue = 'xxx';
		
		beforeEach(angular.mock.module('ngCookies', 'core.authentication'));
		beforeEach(angular.mock.module('ngCookies', 'logout'));
		beforeEach(inject(function(_$rootScope_, $componentController,
				_$location_, _$cookies_, _authenticationService_) {
			$rootScope = _$rootScope_;
			$location = _$location_;
			$cookies = _$cookies_;
			$cookies.put(authHeaderName, authHeaderValue);
			authenticationService = _authenticationService_;
		}));
		
		it('should test logging out', inject(function($componentController) {
			expect($location.path()).toEqual('');
			expect($cookies.get(authHeaderName)).toEqual(authHeaderValue);
			spyOn($rootScope, '$broadcast').and.callThrough();
			
			var ctrl = $componentController('logout');

			expect($location.path()).toBe('/login');
			expect($rootScope.$broadcast).toHaveBeenCalledWith('logOutEvent', null);
			expect($cookies.get(authHeaderName)).toEqual(undefined);
		}));

	});

});