angular
		.module('feedbackPanel')
		.component(
				'feedbackPanel',
				{
					templateUrl : './resources/app/feedback-panel/feedback-panel.template.html',
					controller : [
							'$scope',
							function FeedbackPanelController($scope) {
								var self = this;
								
								$scope.$on('failureEvent', function(event, data) {
									self.error = data.ex;
								});
							} ]
				});