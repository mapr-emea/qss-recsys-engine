/**
 * Created by Marco on 11/30/2014.
 */
'use strict';

angular.module('myApp.config', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/config', {
            templateUrl: 'config/config.html',
            controller: 'ConfigCtrl'
        });
    }])

    .controller('ConfigCtrl', [function ($scope) {

    }]);
