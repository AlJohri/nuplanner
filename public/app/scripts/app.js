'use strict';

angular.module('nuPlannerApp', ['ui.calendar', 'ui.bootstrap']).config(function ($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'views/main.html',
		controller: 'MainCtrl'
	})
	.otherwise({ redirectTo: '/' });
});

angular.module('nuPlannerApp').controller('MainCtrl', function ($scope) {

	$scope.eventSource = {  url: "/events", currentTimezone: 'America/Chicago', data: { query: '' } };
    $scope.eventSources = [$scope.eventSource];

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