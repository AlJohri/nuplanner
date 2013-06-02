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
//java i/o
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
//json
import org.json.*;

import org.json.*;
import org.json.simple.JSONValue;

import com.google.gson.*;

// http://www.avaje.org/ebean/introquery.html

public class Scrape extends Controller {

	/*
		Many events hosted by an organization are AT the organization, thus the Venue ID of the vent
	  	is the same as the Organization ID that created the event.
	*/

	private static final String[] BLOCKED_ORGANIZATIONS = new String[] {
		"7268844551"
	};

	private static final String[] BLOCKED_VENUES = new String[] {
		"17070917171",
		"356690111071371",
		"430645200319091",
		"265779566799135",
		"120569982856",
		"155288914484855",
		"161587597298994",
		"132449680200187",
		"253108394915",
		"110184922344060",
		"115288991957781",
		"143834612313652",
		"111268432255671",
		"44661328399",
		"442292685838282",
		"110432202311659",
		"115421351813697"
	};

	private static final String[] LOCATIONS = new String[] {
		"Lutkin Hall",
		"Ayers CCI",
		"Northwestern University",
		"Buffett Center at Northwestern",
		"Ethel M. Barber Theatre",
		"Norbucks",
		"Harris Hall",
		"Rainbow Alliance",
		"Ethel M. Barber Theatre"
	};



// TODO: change something to use a LinkedList
// http://www.daniweb.com/software-development/java/thrads/379327/how-to-use-linkedlist

// https://developers.facebook.com/docs/facebook-login/access-tokens/#generating
// https://graph.facebook.com/oauth/access_token?client_id=524073037656113&client_secret=7e9db2e6869c8ae6e7bc60d09686d54a&grant_type=client_credentials

	static boolean filter_event(MyEvent event) {
		String jsontext = event.venue;

		try {
			String creator = event.creator;
			JSONObject json = new JSONObject(jsontext);
			String venueID = json.has("id") ? json.get("id").toString() : "";

			boolean blocked_venue = Arrays.asList(BLOCKED_VENUES).contains(venueID);
			boolean blocked_organization = Arrays.asList(BLOCKED_ORGANIZATIONS).contains(creator);

			if (blocked_venue || blocked_organization) return false;

		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}


		return true;
	}

	public static Result scrape_locations() {

		String blah = "";

		for(int i =0; i < LOCATIONS.length; i++) {

			String MY_ACCESS_TOKEN = "CAACEdEose0cBAAy7nvgBDxUJkSbcRepdec05oe8GzFr1EekvA3LvLRgkQXXipYMEsKidMNBqX6rMDYZBGOCjxAUTZCj2zikkzDtObCmLzTwGb8l7NK8FZCG3OloZCGB19mBFWiacEALIZAZCtq7ZCJXN2o567P9ZAzAhpoYmqERicAZDZD";
			FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

			com.restfb.json.JsonObject result = facebookClient.fetchObject( "search", com.restfb.json.JsonObject.class, 
				Parameter.with("q", LOCATIONS[i]), 
				Parameter.with("type", "event")
			);

			com.restfb.json.JsonArray events = result.getJsonArray("data");
			blah += events.toString();


		}

		return ok(blah);


	}

	public static Result scrape_temp() {
		List <MyEvent> eventList = new ArrayList<MyEvent>();

		String MY_ACCESS_TOKEN = "524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		List<MyOrganization> organizations=MyOrganization.find.all();
		for(MyOrganization a:organizations) {
			String query = "SELECT eid, name, description, pic_big, start_time, end_time, location, venue FROM event WHERE creator = " + a.fbid;
			List<FqlEvent> events = facebookClient.executeFqlQuery(query, FqlEvent.class);
			
			for(FqlEvent event:events) {
				//create event class objects
				DateTime starttime = new DateTime();
				DateTime endtime = new DateTime();
				starttime = starttime.parse(event.start_time);
				if (event.end_time!=null && !event.end_time.isEmpty()) endtime = endtime.parse(event.end_time);
				else endtime=null;

				String creator = String.valueOf(a.fbid);
				
				if(MyEvent.findLong.byId(event.eid)==null) {
					MyEvent newEvent = new MyEvent(event.eid,event.name,creator,starttime,endtime,event.location,event.venue,event.description);
					
					if (filter_event(newEvent)) {
						eventList.add(newEvent);
						newEvent.save();
					}
					
				}
				System.out.println(event);
			}
		}

		String jsonText = JSONValue.toJSONString(eventList);

		return ok(jsonText);
	}

	// search?q="lutkin hall"&type=event&fields=name,start_time,end_time,location,id,venue&locale=en_US

	public static Result scrape_graph() {

		String MY_ACCESS_TOKEN = "CAACEdEose0cBAAy7nvgBDxUJkSbcRepdec05oe8GzFr1EekvA3LvLRgkQXXipYMEsKidMNBqX6rMDYZBGOCjxAUTZCj2zikkzDtObCmLzTwGb8l7NK8FZCG3OloZCGB19mBFWiacEALIZAZCtq7ZCJXN2o567P9ZAzAhpoYmqERicAZDZD";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

		// search?q=*&type=event&center="42.052925,-87.665834"&distance=10&fields=name,start_time,end_time,location,id,venue&locale=en_US
		com.restfb.json.JsonObject result = facebookClient.fetchObject( "search", com.restfb.json.JsonObject.class, 
			Parameter.with("q", "\"\""), 
			Parameter.with("center", "42.052925,-87.665834"), 
			Parameter.with("type", "event"), 
			Parameter.with("distance", 1000),
			Parameter.with("limit", 5000) 
		);

		com.restfb.json.JsonArray events = result.getJsonArray("data");
		//result = facebookClient.fetchConnectionPage(result.getNextPageUrl(), JsonObject.class);
		return ok(events.toString());
	}

	public static Result scrape_fql() {
		List <MyEvent> eventList = new ArrayList<MyEvent>();

		String MY_ACCESS_TOKEN = "524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		// com.restfb.json.JsonArray events = new com.restfb.json.JsonArray();  // Change THIS variable to a LinkedList

		String fql_query = 
			"SELECT eid, name, creator, start_time, end_time, description, location, venue FROM event WHERE eid IN\n" +
     			"(SELECT eid FROM event_member WHERE uid IN\n" + 
          			"(SELECT page_id FROM place WHERE distance(latitude, longitude, '42.054581', '-87.677192') < 1500 LIMIT 51000)\n" +
     			"LIMIT 51000)\n" +
			"ORDER BY start_time ASC LIMIT 51000\n";

		List<com.restfb.json.JsonObject> list_events2 = facebookClient.executeFqlQuery(fql_query, com.restfb.json.JsonObject.class);
		com.restfb.json.JsonArray method2_events = new com.restfb.json.JsonArray();

		for (com.restfb.json.JsonObject jsonObject : list_events2) {
			// events.put(jsonObject);
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

			MyEvent newEvent = new MyEvent(eid, jsonObject.getString("name"), jsonObject.getString("creator"), start_dt, end_dt, jsonObject.getString("location"), jsonObject.getString("venue"), jsonObject.getString("description"));

			if (filter_event(newEvent)) {
				if (MyEvent.findLong.byId(eid) == null) {
					eventList.add(newEvent);
					newEvent.save();
				}
			}

		}

		String jsonText = JSONValue.toJSONString(eventList);

		return ok( jsonText );
	}

	public static Result scrape_organizations() {
		String a="";
		for (int i=1;i<52;i++)
		{
		Document doc;
		Document doc1;
		JSONObject json;
		try {
			String[] name =  new String[100];
			String[] name1 = new String[100];
			String[] name2 = new String[100];
			int[] id =new int[100];
			doc = Jsoup.connect("https://northwestern.collegiatelink.net/organizations?SearchType=None&SelectedCategoryId=0&CurrentPage="+ i).get();
			Element sa = doc.getElementById("results");
			Elements sai = sa.getElementsByTag("h5");
			int j=0;
			for(Element e:sai){
				Element f = e.child(0);
				Document doc2=Jsoup.connect("https://northwestern.collegiatelink.net" + f.attr("href")).get();
				Elements fb=doc2.select("a[class*=icon-social facebook]");
				for (Element g:fb){
					if((g.attr("href").length()!=4)){
					name[j]=g.attr("href");//facebook link
					id[j]=name[j].lastIndexOf("/");
					name1[j]=name[j].substring(id[j]+1);//last string after / and groups end with a / in fb link
					if (!name1[j].isEmpty()) {
						name2[j]="https://graph.facebook.com/"+name1[j];//graph url
						System.out.println(name2[j]);
						InputStream is =new URL(name2[j]).openStream();
						try{
						BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						StringBuilder sb = new StringBuilder();
						int cp;
						while((cp=rd.read())!=-1)
							sb.append((char)cp);
						String jsontext=sb.toString();
						try {
							json = new JSONObject(jsontext);
							 a=json.get("id").toString();
							 Long fbid = Long.valueOf(a).longValue();
							 String fbname = json.has("name") ? json.get("name").toString() : "";
							 String location = json.has("location") ? json.get("location").toString() : "";
							 String link = json.has("link") ? json.get("link").toString() : "";
							 String description = json.has("description") ? json.get("description").toString() : "";
							 if (MyOrganization.findLong.byId(fbid) == null) new MyOrganization(fbid, fbname, location, link, description).save();
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							// e1.printStackTrace();
							System.out.println(e1.getMessage());
						}

						}
						finally
						{
							is.close();
						}

					j++;}
				}
			}

			}} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		}
		return ok("done");
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