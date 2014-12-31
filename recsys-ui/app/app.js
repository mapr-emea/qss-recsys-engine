'use strict';

angular.module('myApp.services', ['ngResource'])
.factory('HistoryItems', ['$rootScope','$resource', function($rootScope, $resource) {
	return $resource($rootScope.api + 'history/get', { user_id: $rootScope.data.user_id },{
		forget: { method:'GET', url: $rootScope.api + 'history/forget'}
	});
}])
.factory('RecommendedItems', ['$rootScope','$resource', function($rootScope, $resource) {
	return $resource($rootScope.api + 'recommendations', { user_id: $rootScope.data.user_id })
}])
.factory('Items', ['$rootScope','$resource', function($rootScope, $resource) {
	return $resource($rootScope.api + 'items/:item_id', { user_id: $rootScope.data.user_id},{
  		consume: { method:'GET', url: $rootScope.api + 'items/:item_id/consume', isArray: true},
  		popular: { method:'GET', url: $rootScope.api + 'items/popular', isArray: true},
  		recommended: { method:'GET', url: $rootScope.api + 'recommendations', isArray: true},
  		recommendedFromResults: { method:'GET', url: $rootScope.api + 'recommendations/results', isArray: true},
  		search: { method:'GET', url: $rootScope.api + 'items/search', isArray: true},

  	});
}]);

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.searchresults',
  'myApp.filters',
  'myApp.services',
  'ngToast'
])
.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/home', {
			templateUrl: 'views/home.html',
		})
		.otherwise({redirectTo: '/home'});
}])
.run(function($rootScope, $location) {
	$rootScope.data = {
		search: '',
		user_id: '1',
		itemHistory: [],
		popularItems: [],
		recommendedItems: []
	};
	$rootScope.api = 'http://localhost:8080/api/';

	$rootScope.$watch("data.search", function(newValue, oldValue) {
		if (newValue == "") {
			$location.path('/home');
		} else {
			$location.path('/search/'+newValue);
		}
	});
})
.controller('AppCtrl', ['$scope','$rootScope','HistoryItems','Items', 'ngToast',
	function($scope,$rootScope,HistoryItems, Items, ngToast) {

	$rootScope.$watch("data.itemHistory", function(newValue, oldValue) {
		console.log('Updating the recommended based on history.');
		Items.recommended(function(response) {
			$rootScope.data.recommendedItems = response;
		});
	});


    // actions 

    $scope.consume = function(item_id) {
    	Items.consume({item_id: item_id}, function(response) {
    		console.log('Marking item '+item_id+' as consumed.');
    		$rootScope.data.itemHistory = response;
    		console.log('Updating the HistoryItems from the server.');

    		ngToast.create({
				content: 'Item '+item_id+' consumed.',
				class: 'info',
				horizontalPosition: 'center',
				timeout: 1200
			});
		});
    };

    $scope.forget = function() {
    	HistoryItems.forget(function(response) {
			$rootScope.data.itemHistory = [];
			
			ngToast.create({
				content: 'Resetting history',
				class: 'danger',
				horizontalPosition: 'center',
				timeout: 1800
			});
		})
    	
    	return false;
    };


	// on start

	Items.popular(function(response) {
		$rootScope.data.popularItems = response;
	});

	HistoryItems.query(function(response) {
		$rootScope.data.itemHistory = response;
	});
}]);
