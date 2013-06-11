'use strict';

/**
 * Set up AngularJS Application named "nuPlannerApp". Dependencies are "ui.calendar" and  
 * "ui.bootstrap". Part of the angular-ui suite. The ui.calendar is an encapsulation of 
 * FullCalendar while ui.boostrap is obviously an encapsulation of the Twitter BootStrap
 * framework. The $routeProvider sets the root URL of the application ("/") to use the 
 * "views/main.html" view with the "MainCtrl" controller.
 */
angular.module('nuPlannerApp', ['ui.calendar', 'ui.bootstrap']).config(function ($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'views/main.html',
		controller: 'MainCtrl'
	})
	.otherwise({ redirectTo: '/' });
});

/**
 * Create an AngularJS Controller named "MainCtrl". This controller handles all functionaly within
 * the Main view of the application (views/main.html).
 */
angular.module('nuPlannerApp').controller('MainCtrl', function ($scope) {

	// The eventSource variable connects to the Play Framework's controller "/events" which 
	// provides a JSON feed of events with parameters to filter. Within ui.calendar (FullCalendar)
	// two internal parameters are added to the "/events" URL. These are "start" and "end". The two
	// parameters are unixtimestamps and change based on the current view's start and end dates
	// (e.g. week, month, day). A third parameter, query is added to allow text based filtering of 
	// events based using the title.
	$scope.eventSource = {  url: "/events", currentTimezone: 'America/Chicago', data: { query: '' } };

	// Since FullCalendar can use multiple eventSources, the eventSources variable adds the 
	// eventSource above to the array of eventSources that will be linked to FullCalendar. 
    $scope.eventSources = [$scope.eventSource];

    // This function changes a set of variables within the $scope that represent the selected event
    // and its attributes. It then shows displays the modal for the clicked Event.
    $scope.alertOnEventClick = function( event, jsEvent, view ) {
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

    		$('#eventModal').modal();
    	});
    	return false;
    };

    // This variable sets the 
    $scope.uiConfig = {
    	calendar: {
	        height: 500,
	        editable: false,
	        theme: false,
	        header: {
	          left: 'month, basicWeek, basicDay',
	          center: 'title',
	          right: 'today prev,next'
      		},
			defaultView: 'month',
			aspectRatio: 1,
			dayClick: $scope.alertOnDayClick,
			eventDrop: $scope.alertOnDrop,
			eventResize: $scope.alertOnResize,
			eventClick: $scope.alertOnEventClick
		}
	};

	// This function enables text based filtering by updateing the eventSource's query parameter
	// to the current query variable in the scope. It then refetches the events with the new or 
	// changed parameter. The updateQuery function, as one can see in the "keyup" directive below,
	// is run everytime the user types in the inputbox.
	$scope.updateQuery = function() {
		$scope.eventSource.data.query = $scope.query;
		$scope.myCalendar.fullCalendar('refetchEvents');
	}

});

/**
 * Creates an AngularJS directive named "keyup". As the name suggests, this directive,
 * when applied as an attribute to an element, bind's the element's keyup event.
 * When the keyup event is fired the function listed as an attribute to the keyup function
 * in the HTML is used. In this application, that function is "updateQuery".
 */
angular.module('nuPlannerApp').directive('keyup', function() {
	return function(scope, element, attrs, ctrl) {
		element.bind("keyup", function(event) {
			scope.$apply(attrs.keyup)      
		});
	};
});