package controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Play Framework
import play.*;
import play.mvc.*;
import play.data.*;
import play.db.ebean.*;

import models.*;
import views.html.*;

// http://joda-time.sourceforge.net/apidocs/
import org.joda.time.format.*; // DateTimeFormatter
import org.joda.time.DateTime; // http://joda-time.sourceforge.net/api-release/index.html

// http://restfb.com/javadoc/
import com.restfb.*; // FacebookClient
import com.restfb.FacebookClient.*;
import com.restfb.json.*; // JsonObject, JsonArray, JsonTokener
import com.restfb.types.Event;

// http://jsoup.org/apidocs/
import org.jsoup.*;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.*; // Document, Element
import org.jsoup.select.Elements;

import com.google.gson.*;

// http://www.avaje.org/ebean/introquery.html

public class Scrape extends Controller {

// TODO: change something to use a LinkedList
// http://www.daniweb.com/software-development/java/thrads/379327/how-to-use-linkedlist

// https://developers.facebook.com/docs/facebook-login/access-tokens/#generating
// https://graph.facebook.com/oauth/access_token?client_id=524073037656113&client_secret=7e9db2e6869c8ae6e7bc60d09686d54a&grant_type=client_credentials

	public static Result scrape_events() {
		String MY_ACCESS_TOKEN = "524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		com.restfb.json.JsonArray events = new com.restfb.json.JsonArray();  // Change THIS variable to a LinkedList	
		String fql_query = "SELECT eid, name, creator, start_time, end_time, description, location, venue, pic, pic_big, pic_cover, parent_group_id FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid IN (SELECT page_id FROM place WHERE distance(latitude, longitude, '42.054774', '-87.67654') < 5000 LIMIT 51000) LIMIT 51000) AND venue.id IN (SELECT page_id FROM place WHERE distance(latitude,longitude, '42.054774', '-87.67654') < 5000 LIMIT 51000) ORDER BY start_time ASC LIMIT 51000";
		List<com.restfb.json.JsonObject> list_events2 = facebookClient.executeFqlQuery(fql_query, com.restfb.json.JsonObject.class);
		com.restfb.json.JsonArray method2_events = new com.restfb.json.JsonArray();

		for (com.restfb.json.JsonObject jsonObject : list_events2) {
			events.put(jsonObject);
			method2_events.put(jsonObject);

			DateTimeFormatter formatter;
			if (jsonObject.getString("start_time").length()>19) formatter= DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
			else if (jsonObject.getString("start_time").length()>10) formatter= DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
			else formatter= DateTimeFormat.forPattern("yyyy-MM-dd");

			DateTime start_dt;
			if (jsonObject.getString("start_time").length()!=4) start_dt=formatter.parseDateTime(jsonObject.getString("start_time"));
			else start_dt=null;

			if (jsonObject.getString("end_time").length()>19)  formatter= DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
			else if (jsonObject.getString("end_time").length()>10) formatter= DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
			else formatter= DateTimeFormat.forPattern("yyyy-MM-dd");

			DateTime end_dt;
			if (jsonObject.getString("end_time").length()!=4) end_dt=formatter.parseDateTime(jsonObject.getString("end_time"));
			else end_dt=null;

			Long eid = Long.valueOf(jsonObject.getString("eid")).longValue();

			//create event class objects

			if (MyEvent.findLong.byId(eid) == null) new MyEvent(eid, jsonObject.getString("name"), jsonObject.getString("creator"), start_dt, end_dt, jsonObject.getString("location"), jsonObject.getString("venue"), jsonObject.getString("description")).save();
		}

		return ok( events.toString() );
	}

	public static Result scrape_organizations() {

		List<String> name = new ArrayList<String>(509);

		int j=0;
		for (int i = 1; i < 52; i++) {
			Document doc;
			try {
				doc = Jsoup.connect("https://northwestern.collegiatelink.net/organizations?SearchType=None&SelectedCategoryId=0&CurrentPage="+ i).get();
				Element sa = doc.getElementById("results");
				Elements sai = sa.getElementsByTag("h5");
				for(Element e : sai) {
					Element f = e.child(0);
					Document doc2=Jsoup.connect("https://northwestern.collegiatelink.net" + f.attr("href")).get();
					Elements fb=doc2.select("a[class*=icon-social facebook]");
					for (Element g:fb) {
						name.add(g.attr("href"));
					}
					System.out.println(j); // print number to show stuff is happening
					j++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println(name.toString());
		return ok("lala scrape");
	}

	public static Result scrape_wildcatconnection() {
		String json = "{}";
		try {
			org.jsoup.Connection conn; org.jsoup.Connection.Response res;

			conn = Jsoup.connect("https://northwestern.collegiatelink.net/events");
			conn = conn.data("view", "calendar", "CurrentMonth", "5", "CurrentYear", "2013", "CurrentPage", "1");
			res = conn.method(Method.POST).ignoreContentType(true).execute();
		 	json = res.body();

		 	com.google.gson.JsonElement jelement = new JsonParser().parse(json);
		 	com.google.gson.JsonObject jobject = jelement.getAsJsonObject();
		 	com.google.gson.JsonArray jarray = jobject.getAsJsonArray("Events");

		 	for (com.google.gson.JsonElement object : jarray) {
		 		com.google.gson.JsonObject element = object.getAsJsonObject();
    			String result = element.get("Name").toString();
    			System.out.println(result);
		 	}

		} catch (IOException e) {
			e.printStackTrace();
		}
   		return ok(json);
	}
}