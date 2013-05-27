package controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.data.Form;
// http://www.playframework.com/documentation/api/2.0/java/index.html

import models.*;

import org.apache.commons.io.FileUtils;

import org.joda.time.DateTime;

// http://www.playframework.com/documentation/2.1.1/JavaJsonRequests
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.mvc.BodyParser;

// http://www.playframework.com/documentation/2.1.1/JavaActions
// http://stackoverflow.com/questions/14843365/get-request-parameter-with-play-framework
// http://stackoverflow.com/questions/9808348/request-params-is-gone-in-play-framework-2-0
public class Application extends Controller {

	// http://arshaw.com/fullcalendar/docs/event_data/events_json_feed/
	public static Result events() {

		String start = request().getQueryString("start");
		String end = request().getQueryString("end");

		if (start != null && end != null) {
			DateTime start_time = DateTime.parse(start);
			DateTime end_time = DateTime.parse(end);
		}

		List<MyEvent> events;

		// events = MyEvent.find.where().gt("start_time", start_time).lt("end_time", end_time);
		events = MyEvent.find.all();

		Iterator<MyEvent> iterator = events.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().name);
		}
		return ok( events.toString() );
	}

}
