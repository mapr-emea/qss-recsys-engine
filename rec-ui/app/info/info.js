/**
 * Created by Marco on 11/30/2014.
 */
'use strict';

angular.module('myApp.info', ['ngRoute', 'elasticsearch'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/info', {
            templateUrl: 'info/info.html',
            controller: 'InfoCtrl'
        });
    }])
    .service('client', function (esFactory) {
        return esFactory({
            host: 'localhost:9200',
            apiVerion: '1.4',
            log: 'trace'
        });
    })
    .controller('InfoCtrl', function ($scope, client, esFactory) {
        $scope.message = "Angular ElasticSearch"

        client.cluster.state({
            metric: [
                'cluster_name',
                'nodes',
                'master_node',
                'version'
            ]
        })
            .then(function (resp) {
                $scope.clusterState = resp;
                $scope.error = null;
            })
            .catch(function (err) {
                $scope.clusterState = null;
                $scope.error = err;
                // if the err is a NoConnections error, then the client was not able to
                // connect to elasticsearch. In that case, create a more detailed error
                // message
                if (err instanceof esFactory.errors.NoConnections) {
                    $scope.error = new Error('Unable to connect to elasticsearch. ' +
                    'Make sure that it is running and listening at http://localhost:9200');
                }
            });
    });
