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

import org.joda.time.format.*; // DateTimeFormatter
import org.joda.time.DateTime;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Application extends Controller {
  
    public static Result index() {
		return redirect(routes.Application.scrape_fast());
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
	// http://www.daniweb.com/software-development/java/thrads/379327/how-to-use-linkedlist

	public static Result scrape_fast() {
		String MY_ACCESS_TOKEN = "CAAHcpEMZB8DEBAIWARfeZBZA4vrZA47l0FGrBFXyrF7ViWYjZCA3CyHhGgD8hIINGuaZBzrXFs73YEqpg4kRIXdv3BNhf5MzCekpGTBNh9xjFwjFpZBmQ7fkg2zKSJgZBBFv3q2c7zbEtD9qQrRgqlxkMREuLSpiBUa8WdeRaoHJlwZDZD";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		JsonArray events = new JsonArray();  // Change THIS variable to a LinkedList	
		String fql_query = "SELECT eid, name, creator, start_time, end_time, description, location, venue, pic, pic_big, pic_cover, parent_group_id FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid IN (SELECT page_id FROM place WHERE distance(latitude, longitude, '42.054774', '-87.67654') < 5000 LIMIT 51000) LIMIT 51000) AND venue.id IN (SELECT page_id FROM place WHERE distance(latitude,longitude, '42.054774', '-87.67654') < 5000 LIMIT 51000) ORDER BY start_time ASC LIMIT 51000";
		List<JsonObject> list_events2 = facebookClient.executeFqlQuery(fql_query, JsonObject.class);
		JsonArray method2_events = new JsonArray();

		for (JsonObject jsonObject : list_events2) {
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

			//create event class objects
		    new MyEvent(jsonObject.getString("name"), jsonObject.getString("creator"), start_dt, end_dt, jsonObject.getString("location"), jsonObject.getString("venue"), jsonObject.getString("description")).save();
		}

		return ok( events.toString() );
	}

	public static Result scrape_slow() {
		String[] name = new String[1000];

		int j=0;
		for (int i = 1; i < 52; i++) {
			Document doc;
			try {
				doc = Jsoup.connect("https://northwestern.collegiatelink.net/organizations?SearchType=None&SelectedCategoryId=0&CurrentPage="+ i).get();
				Element sa = doc.getElementById("results");
				Elements sai = sa.getElementsByTag("h5");
				for(Element e : sai) {
					Element f = e.child(0);
					name[j]=f.text();
					Document doc2=Jsoup.connect("https://northwestern.collegiatelink.net" + f.attr("href")).get();
					Elements fb=doc2.select("a[class*=icon-social facebook]");
					for (Element g:fb) {
						name[j]=name[j] + g.attr("href");
					}
					//System.out.println(name[j]);
					System.out.println(j); // print number
					j++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Arrays.toString(name));
		return ok("lala scrape");
	}


}
