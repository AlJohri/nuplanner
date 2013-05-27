'use strict';

angular.module('nuPlannerApp', ['ui.calendar']).

  config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'CalendarCtrl'
      })
      .when('/list',{
        templateUrl: 'views/list.html',
        controller: 'ListCtrl'
      })
      .otherwise({ redirectTo: '/' });
  });