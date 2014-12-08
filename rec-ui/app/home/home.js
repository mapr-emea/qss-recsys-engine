/**
 * Created by Marco on 11/29/2014.
 */

'use strict';

angular.module('myApp.home', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/home', {
            templateUrl: 'home/home.html',
            controller: 'HomeCtrl'
        });
    }])
    .service('client', function (esFactory) {
        return esFactory({
            host: 'localhost:9200',
            apiVerion: '1.4',
            log: 'trace'
        });
    })
    .controller('HomeCtrl', function ($scope, client, esFactory) {

        $scope.es_search = function () {
            $scope.searchResponse = null;

            client.search({
                index: 'movies',
                size: '30',
                type: 'film',
                q: $scope.searchTerm

            }).then(function (resp) {
                var hits = resp.hits.hits;
                $scope.searchResponse = resp;
                console.log(resp);

            }, function (err) {
                console.trace(err.message);

            });
        }; // es_search

    }); // controller