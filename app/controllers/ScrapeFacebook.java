package controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import play.*; // Play Framework
import play.mvc.*;
import play.data.*;
import play.db.ebean.*; // http://www.avaje.org/ebean/introquery.html
import models.*;
import views.html.*;
import org.joda.time.format.*; // DateTimeFormatter http://joda-time.sourceforge.net/apidocs/
import org.joda.time.DateTime; // http://joda-time.sourceforge.net/api-release/index.html
import com.restfb.*; // FacebookClient // http://restfb.com/javadoc/
import com.restfb.FacebookClient.*;
import com.restfb.json.*; // JsonObject, JsonArray, JsonTokener
import com.restfb.types.Event;
import org.jsoup.*; // http://jsoup.org/apidocs/
import org.jsoup.Connection.Method;
import org.jsoup.nodes.*; // Document, Element
import org.jsoup.select.Elements;
import java.io.BufferedReader; // java i/o
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.*; // json
import org.json.simple.JSONValue;
import com.google.gson.*;
import java.util.concurrent.atomic.AtomicReference;

/** The ScrapeFacebook class scrapes Facebook for events using various different methods including
  * fql, graph api, finding events created by specific organizations, and lastly querying based on location.
  *
*/

public class ScrapeFacebook extends Controller {

    public static Result scrape_facebook() {
        scrape_fql();
        // scrape_graph();
        scrape_organizations();
        scrape_locations();

        return ok("hi");
    }

    private static final String MY_ACCESS_TOKEN = "524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk";
    private static final String MY_OTHER_TOKEN = "CAACEdEose0cBACMe8VTWnK4R4SHNAXLq5zZBmSsx7q06PZBJybb2BvDdvQW3MgnKtnxGlW38RhIv6F8gam26xZBjzYubPrvSegmt28cfSrHKqDtIzFKv0z1Elvu4lXx9Wtq7RsDLGoZBZA6INvLH0q3VIAgBcHo4wPyK0FtatbwZDZD";

    public static Result scrape_locations() {
        List <MyEvent> eventList = new ArrayList<MyEvent>();

        ArrayList<String> locations = Utilities.getLocations();
        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

        for (int i =0; i < locations.size(); i++) {

            com.restfb.json.JsonObject result = facebookClient.fetchObject( "search", com.restfb.json.JsonObject.class, 
                Parameter.with("q", locations.get(i)), 
                Parameter.with("type", "event"),
                Parameter.with("fields","id,name,owner,description,start_time,end_time,location,venue,privacy,updated_time,picture,ticket_uri")
            );

            com.restfb.json.JsonArray single_location_events = result.getJsonArray("data");

            for(int j = 0; j < single_location_events.length(); j++) {
                com.restfb.json.JsonObject event = (com.restfb.json.JsonObject) single_location_events.get(j);

                MyEvent newEvent = Utilities.createEvent(event);

                Long eid = Long.valueOf(event.getString("id")).longValue();

                if (Utilities.saveOrUpdate(eid, newEvent)) {
                    eventList.add(newEvent);
                }
            }

        }

        String jsonText = JSONValue.toJSONString(eventList);

        return ok(jsonText);
    }

    public static Result scrape_organizations() {
        List <MyEvent> eventList = new ArrayList<MyEvent>();

        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
        List<MyOrganization> organizations = MyOrganization.find.all();
        for(MyOrganization a:organizations) {
            String query = "SELECT eid, name, description, pic_big, start_time, end_time, location, venue FROM event WHERE creator = " + a.fbid;
            List<FqlEvent> events = facebookClient.executeFqlQuery(query, FqlEvent.class);

            for(FqlEvent event:events) {
                Long eid = Long.valueOf(event.eid).longValue();
                MyEvent newEvent = Utilities.createEvent(event);
                if (Utilities.saveOrUpdate(eid, newEvent)) {
                    eventList.add(newEvent);
                }
            }
        }

        String jsonText = JSONValue.toJSONString(eventList);

        return ok(jsonText);
    }

    public static Result scrape_graph() {
        FacebookClient facebookClient = new DefaultFacebookClient(MY_OTHER_TOKEN);
        com.restfb.json.JsonObject result = facebookClient.fetchObject( "search", com.restfb.json.JsonObject.class, 
            Parameter.with("q", "*"), 
            Parameter.with("type", "event"), 
            Parameter.with("center", "42.052925,-87.665834"), 
            Parameter.with("distance", 1000),
            Parameter.with("fields","id,name,owner,description,start_time,end_time,location,venue,privacy,updated_time,picture,ticket_uri")
        );
        com.restfb.json.JsonArray events = result.getJsonArray("data");

        return ok(events.toString());
    }

    public static Result scrape_fql() {
        List <MyEvent> eventList = new ArrayList<MyEvent>();
        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

        String fql_query = 
            "SELECT eid, name, creator, start_time, end_time, description, location, venue FROM event WHERE eid IN\n" +
                "(SELECT eid FROM event_member WHERE uid IN\n" + 
                    "(SELECT page_id FROM place WHERE distance(latitude, longitude, '42.054581', '-87.677192') < 1500 LIMIT 51000)\n" +
                "LIMIT 51000)\n" +
            "ORDER BY start_time ASC LIMIT 51000\n";

        List<com.restfb.json.JsonObject> events = facebookClient.executeFqlQuery(fql_query, com.restfb.json.JsonObject.class);

        for (com.restfb.json.JsonObject event : events) {

            String str_eid = event.has("id") ? event.getString("id") : (event.has("eid") ? event.getString("eid") : "");
            Long eid = Long.valueOf(str_eid).longValue();

            MyEvent newEvent = Utilities.createEvent(event);
            if (Utilities.saveOrUpdate(eid, newEvent)) { 
                eventList.add(newEvent); 
            }

        }

        String jsonText = JSONValue.toJSONString(eventList);
        return ok( jsonText );
    }

    public static com.restfb.json.JsonObject venue_location(String venueID) {
        FacebookClient facebookClient = new DefaultFacebookClient(MY_OTHER_TOKEN);
        com.restfb.json.JsonObject result = facebookClient.fetchObject( venueID, com.restfb.json.JsonObject.class
            //Parameter.with("q", "220438191333027")
        );
        com.restfb.json.JsonObject location = result.getJsonObject("location");

        return location;
    }

}


// create event class objects

// Parameter.with("limit", 5000)
// result = facebookClient.fetchConnectionPage(result.getNextPageUrl(), JsonObject.class);

// Long eid = Long.valueOf(event.get("id").toString());
// System.out.println(eid);

// create event class objects


// TODO: change something to use a LinkedList
// http://www.daniweb.com/software-development/java/thrads/379327/how-to-use-linkedlist

// https://developers.facebook.com/docs/facebook-login/access-tokens/#generating
// https://graph.facebook.com/oauth/access_token?client_id=524073037656113&client_secret=7e9db2e6869c8ae6e7bc60d09686d54a&grant_type=client_credentials

// https://graph.facebook.com/search?q="lutkin hall"&type=event&fields=name,start_time,end_time,location,id,venue&locale=en_US&access_token=524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk
// https://graph.facebook.com/search?q=*&type=event&center="42.052925,-87.665834"&distance=10&fields=name,start_time,end_time,location,id,venue&locale=en_US&access_token=524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk


/**
  * Many events hosted by an organization are AT the organization, thus the Venue ID of the event
  * is the same as the Organization ID that created the event.
*/

// https://developers.facebook.com/docs/reference/api/event/
