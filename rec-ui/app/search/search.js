/**
 * Created by Marco on 11/29/2014.
 */

'use strict';
angular.module('myApp.search', ['ngRoute', 'elasticsearch'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/search', {
            templateUrl: 'search/search.html',
            controller: 'SearchCtrl'
        });

    }])
    .service('client', function (esFactory) {
        return esFactory({
            host: 'localhost:9200',
            apiVerion: '1.4',
            log: 'trace'
        });
    })
    .controller('SearchCtrl', function ($scope, client, esFactory) {

        $scope.searchById = function () {

            $scope.searchResponse = null;
//TODO: replace full text search with filter to do exact match on _id
            client.search({
                index: 'movies',
                type: 'film',
                body: {
                    query: {
                        match: {
                            _id: $scope.searchTerm
                        }
                    }
                }
            }).then(function (resp) {
                var hits = resp.hits.hits;
                $scope.searchResponse = resp;
                console.log(resp);
                $scope.error = null;
            }, function (err) {
                console.trace(err.message);
            });
        };

        $scope.testSearchId = function (item) {
            console.log("fetching item: " + item);
            /*
            $scope.searchResponse2 = null;
//TODO: replace full text search with filter to do exact match on _id
            client.search({
                index: 'movies',
                type: 'film',
                body: {
                    query: {
                        match: {
                            _id: item
                        }
                    }
                }
            }).then(function (resp) {
              //  var hits = resp.hits.hits;
               // $scope.searchResponse2 = resp;
                //console.log(resp);
                console.log("fetching:" + item)

                //$scope.error = null;
            }, function (err) {
                console.trace(err.message);
            });
            */
        };
});