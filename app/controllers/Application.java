package controllers;

import play.*;
import play.mvc.*;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import views.html.*;

import models.*;
import play.data.*;

import com.restfb.*; // http://restfb.com/javadoc/
import com.restfb.json.JsonObject;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonTokener;
import com.restfb.FacebookClient.*;
import com.restfb.types.Event;

import java.util.*;

public class Application extends Controller {
  
    public static Result index() {
        return redirect(routes.Application.events());
    }

	public static Result students() {
	  return ok( "lala" /* views.html.index.render(User.all()) */ );
	}

   public static Result phones(String phoneId) throws IOException {
    File jsonFile = Play.application().getFile("public/phones/"+phoneId);
    String json = FileUtils.readFileToString(jsonFile);
    return ok(json).as("application/json");
  }


  // Change something to use a LinkedList
	// http://www.daniweb.com/software-development/java/threads/379327/how-to-use-linkedlist
	
	public static Result events() {
		String MY_ACCESS_TOKEN = "CAACEdEose0cBAGOZCa7mJoEZCSPZBNn6geHlruiZAiwmv2BAYZB553DEMW7g5ZCJfjkZC3P1JxmI9HpT9GbiXIcQilCIOGb8fFDJYeZC53F0YxCtTpFLCwdLoF8jlQBE65xt8hccWKhJ0v9nrfSKaVOe60ZAImQ6YK5IZD";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

		// Loop through connection object : https://groups.google.com/d/msg/restfb/eHMSgUxEXi4/gemE6_meNyAJ
		
		// Facebook Graph API (Method 1)

		Connection<JsonObject> connection = facebookClient.fetchConnection( 
			"search", JsonObject.class, 
			Parameter.with("q", "*"), 
			Parameter.with("center", "42.052925,-87.665834"), 
			Parameter.with("type", "event"),
			Parameter.with("distance", 10000),
			Parameter.with("fields", "id,owner,picture,description,location,venue,start_time,end_time")
			// Parameter.with("limit", 100)
		);

		JsonArray events = new JsonArray();  // Change THIS variable to a LinkedList

		JsonArray method1_events = new JsonArray();
		do {
			//events.put(connection.getData());
  			for (JsonObject jsonObject : connection.getData()) { 
  				events.put(jsonObject);
  				method1_events.put(jsonObject);
  			}
			connection = facebookClient.fetchConnectionPage(connection.getNextPageUrl(), JsonObject.class);
		} while (connection.hasNext());
		
		System.out.println("Method 1");
		System.out.println(method1_events.toString());

		// FQL Query (Method 2)
		
		String fql_query = "SELECT eid, name, creator, start_time, end_time, description, location, venue, pic, pic_big, pic_cover, parent_group_id FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid IN (SELECT page_id FROM place WHERE distance(latitude, longitude, '42.054774', '-87.67654') < 5000 LIMIT 51000) LIMIT 51000) AND venue.id IN (SELECT page_id FROM place WHERE distance(latitude,longitude, '42.054774', '-87.67654') < 5000 LIMIT 51000) ORDER BY start_time ASC LIMIT 51000";
		List<JsonObject> list_events2 = facebookClient.executeFqlQuery(fql_query, JsonObject.class);

		JsonArray method2_events = new JsonArray();
		for (JsonObject jsonObject : list_events2) {
			events.put(jsonObject);
			method2_events.put(jsonObject);
		}

		System.out.println("Method 2:");
		System.out.println(method2_events.toString());		

		// Method 3
		
		// lalala
		// lalalla
		

		// Remove Duplicates

		System.out.println(events.toString());

		return ok( events.toString() );
	}

}
