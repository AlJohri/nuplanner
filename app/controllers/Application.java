package controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;

import models.*;
// import views.html.*;

import org.apache.commons.io.FileUtils;

// http://www.playframework.com/documentation/2.1.1/JavaJsonRequests
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.mvc.BodyParser;

// TODO: change something to use a LinkedList
// http://www.daniweb.com/software-development/java/thrads/379327/how-to-use-linkedlist

// TODO: split into multiple files
// http://www.playframework.com/documentation/2.1.1/JavaActions
public class Application extends Controller {
  
    public static Result index() {
		return redirect(routes.Scrape.scrape_events());
    }

	public static Result students() {
		return ok( "lala" );
		// views.html.index.render(User.all())
	}

	public static Result phones(String phoneId) throws IOException {
    	File jsonFile = Play.application().getFile("public/phones/"+phoneId);
    	String json = FileUtils.readFileToString(jsonFile);
    	return ok(json).as("application/json");
	}

	public static Result events() {
		List<MyEvent> events = MyEvent.find.all();
		Iterator<MyEvent> iterator = events.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().name);
		}
		return ok( events.toString() );
	}

}
