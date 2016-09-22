describe('menuPanel', function() {

	beforeEach(module('menuPanel'));

	describe('MenuPanelController', function() {

		var $rootScope;
		var $ctrl;
		var $scope;
		var $cookies;
		var authHeaderName = 'Authentication';
		var authHeaderValue = 'xxx';
		
		beforeEach(angular.mock.module('ngCookies', 'menuPanel'));
		beforeEach(inject(function(_$rootScope_, _$cookies_) {
			$cookies = _$cookies_;
			$cookies.remove(authHeaderName);
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
		}));
		
		it('should test isLoggedIn property initialization', inject(function(
				$componentController) {
			var $ctrl = $componentController('menuPanel');
			expect($ctrl.isLoggedIn).toEqual(false);
			$cookies.put('Authentication', authHeaderValue);
			
			$ctrl = $componentController('menuPanel');
			expect($ctrl.isLoggedIn).toEqual(true);
		}));

		it('should test isLoggedIn property on logInEvent', inject(function(
				$componentController) {
			var $ctrl = $componentController('menuPanel');
			expect($ctrl.isLoggedIn).toEqual(false);
			
			$rootScope.$broadcast('logInEvent', null);
			expect($ctrl.isLoggedIn).toEqual(true);
		}));
		
		it('should test isLoggedIn property on logOutEvent', inject(function(
				$componentController) {
			$cookies.put('Authentication', authHeaderValue);
			var $ctrl = $componentController('menuPanel');
			expect($ctrl.isLoggedIn).toEqual(true);
			
			$rootScope.$broadcast('logOutEvent', null);
			expect($ctrl.isLoggedIn).toEqual(false);
		}));
	});

});