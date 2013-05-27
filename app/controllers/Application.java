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
// import org.codehaus.jackson.JsonNode;
// import org.codehaus.jackson.node.ObjectNode;
// import play.mvc.BodyParser;

import com.avaje.ebean.ExpressionList;

// http://www.playframework.com/documentation/2.1.1/JavaActions
// http://stackoverflow.com/questions/14843365/get-request-parameter-with-play-framework
// http://stackoverflow.com/questions/9808348/request-params-is-gone-in-play-framework-2-0
public class Application extends Controller {

	// http://arshaw.com/fullcalendar/docs/event_data/events_json_feed/
	public static Result events() {
		String start = request().getQueryString("start"); String end = request().getQueryString("end");

		// these parameters don't work right now
		ExpressionList<MyEvent> events = MyEvent.findDate.where();
		events = (start != null) ? events.gt("start_time", DateTime.parse(start)) : events;
		events = (end != null) ? events.lt("end_time", DateTime.parse(end)) : events;

		List<MyEvent> eventList = events.findList();
		eventList = (start == null && end == null) ? MyEvent.find.all() : eventList;

		Iterator<MyEvent> itr = eventList.iterator();
		while (itr.hasNext()) {
			itr.next(); // System.out.println(itr.next().name);
		}
		return ok( eventList.toString() );
	}

}
