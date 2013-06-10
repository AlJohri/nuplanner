'use strict';

angular.module('nuPlannerApp', ['ui.calendar', 'ui.bootstrap']).
  config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({ redirectTo: '/' });
  });