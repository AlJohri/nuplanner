package controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import play.*;
import play.mvc.*;
import play.data.*;
import play.db.ebean.*;
import models.*;
import views.html.*;
import org.joda.time.format.*;
import org.joda.time.DateTime;
import com.restfb.*;
import com.restfb.FacebookClient.*;
import com.restfb.json.*;
import com.restfb.types.Event;
import org.jsoup.*;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.*;
import org.json.simple.JSONValue;
import com.google.gson.*;
import java.util.concurrent.atomic.AtomicReference;

public class ScrapeFacebook extends Controller {

    private static final String MY_ACCESS_TOKEN = "524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk";

    public static Result scrape_facebook() {
        System.out.println("FQL"); scrape_fql();
        System.out.println("Organizations"); scrape_organizations();
        System.out.println("Locations"); scrape_locations();
        System.out.println("Complete");
        return ok("scrape");
    }

    public static Result scrape_fql() {
        List <MyEvent> eventList = new ArrayList<MyEvent>();
        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

        String fql_query =
            "SELECT eid, name, creator, start_time, end_time, description, location, venue , pic FROM event WHERE eid IN\n" +
                "(SELECT eid FROM event_member WHERE uid IN\n" +
                    "(SELECT page_id FROM place WHERE distance(latitude, longitude, '42.054581', '-87.677192') < 1500 LIMIT 51000)\n" +
                "LIMIT 51000)\n" +
            "ORDER BY start_time ASC LIMIT 51000\n";

        List<com.restfb.json.JsonObject> events = facebookClient.executeFqlQuery(fql_query, com.restfb.json.JsonObject.class);

        for (com.restfb.json.JsonObject event : events) {
            MyEvent newEvent = Utilities.createEvent(event);
            if (Utilities.filter_event(newEvent) && Utilities.saveOrUpdate(newEvent)) { eventList.add(newEvent); }
        }

        String jsonText = JSONValue.toJSONString(eventList);
        return ok( jsonText );
    }

    public static Result scrape_organizations() {
        List <MyEvent> eventList = new ArrayList<MyEvent>();

        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
        List<MyOrganization> organizations = MyOrganization.find.all();
        for(MyOrganization organization:organizations) {
            String query = "SELECT eid, name, description, start_time, end_time, location, venue, pic FROM event WHERE creator = " + organization.fbid + "LIMIT 51000";
            List<FqlEvent> events = facebookClient.executeFqlQuery(query, FqlEvent.class);

            for(FqlEvent event:events) {
                MyEvent newEvent = Utilities.createEvent(event);
                if (Utilities.filter_event(newEvent) && Utilities.saveOrUpdate(newEvent)) { eventList.add(newEvent); }
            }
        }

        String jsonText = JSONValue.toJSONString(eventList);

        return ok(jsonText);
    }

    public static Result scrape_locations() {
        List <MyEvent> eventList = new ArrayList<MyEvent>();

        ArrayList<String> locations = Utilities.getLocations();
        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

        for (int i = 0; i < locations.size(); i++) {

            com.restfb.json.JsonObject result = facebookClient.fetchObject( "search", com.restfb.json.JsonObject.class,
                Parameter.with("q", locations.get(i)),
                Parameter.with("type", "event"),
                Parameter.with("fields","id,name,owner,description,start_time,end_time,location,venue,privacy,updated_time,picture,ticket_uri"),
                Parameter.with("limit", 5000)
            );

            com.restfb.json.JsonArray single_location_events = result.getJsonArray("data");

            for(int j = 0; j < single_location_events.length(); j++) {
                com.restfb.json.JsonObject event = (com.restfb.json.JsonObject) single_location_events.get(j);
                MyEvent newEvent = Utilities.createEvent(event);
                if (Utilities.filter_event(newEvent) && Utilities.saveOrUpdate(newEvent)) {
                    eventList.add(newEvent);
                }
            }
        }

        String jsonText = JSONValue.toJSONString(eventList);

        return ok(jsonText);
    }

    public static com.restfb.json.JsonObject venue_location(String venueID) {
        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
        com.restfb.json.JsonObject result = facebookClient.fetchObject( venueID, com.restfb.json.JsonObject.class);
        com.restfb.json.JsonObject location = result.getJsonObject("location");
        return location;
    }

    public static Result scrape_graph() {
        String MY_OTHER_TOKEN = "CAAHcpEMZB8DEBANQkZAyhJCXEYnAn2UW9paEZC7VWGJCb9a1ouGismLu8xKfCZCZBdtAl3Ld4iVC6u2HaXHqmknogy4RMuMswUMqc17PKSwAt4UmQoqDT4RIHKQaeHpnoXjXkCWTFlx7fNKKvDLUaauyS3KIuFsvziJaCphDuCgZDZD";
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

}
