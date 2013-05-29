'use strict';

angular.module('nuPlannerApp', ['ui.calendar']).
  config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({ redirectTo: '/' });
  });