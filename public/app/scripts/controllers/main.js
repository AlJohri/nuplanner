'use strict';

angular.module('nuPlannerApp').controller('MainCtrl', function ($scope) {

    $scope.eventSource = { 
      url: "/events", 
      currentTimezone: 'America/Chicago',
      data: { query: '' }
    };

    $scope.eventSources = [$scope.eventSource]; //$scope.events, $scope.eventSource, $scope.eventsF

    /* alert on eventClick */
    $scope.alertOnEventClick = function( event, jsEvent, view ){
        $scope.$apply(function() {
            $scope.selected_event_id = event.id;
            $scope.selected_event_title = event.title;
            $scope.selected_event_allDay = event.allDay;
            $scope.selected_event_creator = event.creator;
            $scope.selected_event_start = event.start;            
            $scope.selected_event_end = event.end;
            $scope.selected_event_description = event.description;
            $scope.selected_event_pic = event.pic;
            $scope.selected_event_url = event.url;
          
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
    
    $scope.addEvent = function() { 
      $scope.events.push({ title: 'Open Sesame', start: new Date(y, m, 28), end: new Date(y, m, 29) }); 
    };

    $scope.remove = function(index) { 
      $scope.events.splice(index,1);  
    };

    $scope.changeView = function(view) { 
      $scope.myCalendar.fullCalendar('changeView',view); 
      $scope.myCalendar.fullCalendar('refetchEvents');
    }; 

    $scope.refetchEvents = function(view) { $scope.myCalendar.fullCalendar('refetchEvents'); }; 
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

    $scope.updateQuery = function() {
      $scope.eventSource.data.query = $scope.query;
      console.log($scope.eventSource.data);
      $scope.myCalendar.fullCalendar('refetchEvents');
    }

});

angular.module('nuPlannerApp').directive('keyup', function() {
  return function(scope, element, attrs, ctrl) {
    element.bind("keyup", function(event) {
      scope.$apply(attrs.keyup)      
    });
  };
});

angular.module('nuPlannerApp').directive('enter', function() {
  return function(scope, element, attrs) {
    element.bind("mouseover", function() {
      console.log("hello");
    });
  };
});