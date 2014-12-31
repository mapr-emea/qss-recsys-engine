'use strict';

angular.module('myApp.searchresults', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/search/:query', {
    templateUrl: 'searchresults/searchresults.html',
    controller: 'SearchResultsCtrl'
  });
}])
.controller('SearchResultsCtrl', ['$scope', '$routeParams', 'Items', function($scope, $routeParams, Items) {
	$scope.data = {
		page: 0,
		query: $routeParams.query,
		resultsRecommendedItems: []
	};

	Items.search({q: $scope.data.query},function(response) {
		$scope.data.resultItems = response;

		var result_ids = $scope.data.resultItems.map(function(item) { return item.id });
		
		if (result_ids.length > 0) {
			Items.recommendedFromResults({ids: result_ids.join(',')}, function(response) {
				$scope.data.resultsRecommendedItems = response;
			});
		}
	});
}]);