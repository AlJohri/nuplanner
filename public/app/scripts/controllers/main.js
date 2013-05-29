'use strict';

angular.module('nuPlannerApp').controller('MainCtrl', function ($scope) {

    /*////////////////////////////////////////////////////

    Main

    ////////////////////////////////////////////////////*/



    /*////////////////////////////////////////////////////

    Calendar

    ////////////////////////////////////////////////////*/

    // var date = new Date();
    // var d = date.getDate();
    // var m = date.getMonth();
    // var y = date.getFullYear();
    /* event source that pulls from google.com */
    // $scope.eventSource = {
    //         url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",
    //         className: 'gcal-event',           // an option!
    //         currentTimezone: 'America/Chicago' // an option!
    // };
    /* event source that contains custom events on the scope */
    // $scope.events = '/events';
    /* event source that calls a function on every view switch */
    // $scope.eventsF = function (start, end, callback) {
    //   var s = new Date(start).getTime() / 1000;
    //   var e = new Date(end).getTime() / 1000;
    //   var m = new Date(start).getMonth();
    //   var events = [{title: 'Feed Me ' + m,start: s + (50000),end: s + (100000),allDay: false, className: ['customFeed']}];
    //   callback(events);
    // };

    $scope.eventSource = {
            url: "http://localhost:9000/events",
            // className: 'gcal-event',           // an option!
            currentTimezone: 'America/Chicago' // an option!
    };

    /* alert on eventClick */
    $scope.alertOnEventClick = function( event, jsEvent, view ){
        $scope.$apply(function() {
          if (event.url) { window.open(event.url); }
        });
        return false;
    };
    /* alert on dayClick */
    $scope.alertOnDayClick = function( date, allDay, jsEvent, view ){
        $scope.$apply(function(){
          // $scope.alertMessage = ('Day Clicked ' + date);
        });
    };
    /* alert on Drop */
     $scope.alertOnDrop = function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
        $scope.$apply(function(){
          $scope.alertMessage = ('Event Droped to make dayDelta ' + dayDelta);
        });
    };
    /* alert on Resize */
    $scope.alertOnResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
        $scope.$apply(function(){
          $scope.alertMessage = ('Event Resized to make dayDelta ' + minuteDelta);
        });
    };
    /* add and removes an event source of choice */
    $scope.addRemoveEventSource = function(sources,source) {
      var canAdd = 0;
      angular.forEach(sources,function(value, key){
        if(sources[key] === source){
          sources.splice(key,1);
          canAdd = 1;
        }
      });
      if(canAdd === 0){
        sources.push(source);
      }
    };
    /* add custom event*/
    $scope.addEvent = function() {
      $scope.events.push({
        title: 'Open Sesame',
        start: new Date(y, m, 28),
        end: new Date(y, m, 29),
        className: ['openSesame']
      });
    };
    /* remove event */
    $scope.remove = function(index) {
      $scope.events.splice(index,1);
    };
    /* Change View */
    $scope.changeView = function(view) {
      $scope.myCalendar.fullCalendar('changeView',view);
    };
    /* config object */
    $scope.uiConfig = {
      calendar:{
        // height: 1000,
        editable: false,
        theme: false,
        header: {
          left: '', // month basicWeek basicDay agendaWeek agendaDay
          center: 'title', // title
          right: 'today prev,next' // today prev,next
        },
        defaultView: 'month',
        aspectRatio: 1,
        dayClick: $scope.alertOnDayClick,
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize,
        eventClick: $scope.alertOnEventClick
      }
    };
    /* event sources array*/
	$scope.eventSources = [$scope.eventSource]; //$scope.events, $scope.eventSource, $scope.eventsF


});