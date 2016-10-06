describe('feedbackPanel', function() {

	beforeEach(module('feedbackPanel'));

	describe('FeedbackPanelController', function() {
		
		var $rootScope;
		var $scope;
		var $ctrl;
		
		beforeEach(inject(function($componentController, _$rootScope_) {
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$ctrl = $componentController('feedbackPanel', { $scope : $scope });
		}));
		
		it('should test controllers failureEvent handling', inject(function($componentController) {
			var errorInfo = {
					url : 'url',
					ex : 'error'
			};
			
			expect($ctrl.error).toEqual(undefined);
			$rootScope.$broadcast('failureEvent', errorInfo);
			expect($ctrl.error).toEqual(errorInfo.ex);
		}));

	});

});