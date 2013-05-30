'use strict';

angular.module('nuPlannerApp').controller('MainCtrl', function ($scope) {

    /*////////////////////////////////////////////////////

    Main

    ////////////////////////////////////////////////////*/



    /*////////////////////////////////////////////////////

    Calendar

    ////////////////////////////////////////////////////*/

    $scope.eventSource = { url: "/events", currentTimezone: 'America/Chicago' };
    $scope.eventSources = [$scope.eventSource]; //$scope.events, $scope.eventSource, $scope.eventsF

    /* alert on eventClick */
    $scope.alertOnEventClick = function( event, jsEvent, view ){
        $scope.$apply(function() {
          if (event.url) { window.open(event.url); }
          // https://support.google.com/calendar/answer/3033039
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
    
    $scope.addEvent = function() { $scope.events.push({ title: 'Open Sesame', start: new Date(y, m, 28), end: new Date(y, m, 29) }); };
    $scope.remove = function(index) { $scope.events.splice(index,1);  };
    $scope.changeView = function(view) { $scope.myCalendar.fullCalendar('changeView',view); }; 
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


});