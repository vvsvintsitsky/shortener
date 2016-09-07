mainApp.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
   $routeProvider.
   
   when('/login', {
      template: '<login></login>'
   }).
   
   when('/viewUrls', {
      template: '<view-urls></view-urls>'
   }).
   
   when('/info/:urlToView', {
      template: '<url></url>'
   }).
   
   when('/tag/:tagDescription', {
      template: '<tag-urls></tag-urls>'
   }).
   
   otherwise({
      redirectTo: '/login'
   });

}]);