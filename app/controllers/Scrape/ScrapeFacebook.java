package controllers;

/* General Java */
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

/* Play Frameowork */
import play.*;
import play.mvc.*;
import play.data.*;
import play.db.ebean.*;
import models.*;
import views.html.*;

/* JSON */
import org.json.simple.JSONValue;

/* RestFB */
import com.restfb.*;
import com.restfb.FacebookClient.*;
import com.restfb.json.*;

/* FB Constants and Utilities */
import static controllers.Facebook.*;
import static controllers.Utilities.*;

public class ScrapeFacebook extends Scrape {

    /**
     * Scrapes events from Facebook using the Facebook Graph API. It searches for events
     * radially outward from predefined center coordinates with a predefined radius.
     * <p>
     * This method requires a USER_ACCESS_TOKEN as opposed to the standard APP_ACCESS_TOKEN.
     * Thus, this method can only be run upon user authentication. Thus, this method is 
     * currently not being used in the "scrape sequence," the sequence of functions run to 
     * scrape all events (defined in Scrape.java).
     * <p>
     * TODO: Enable this function after implementing Facebook User Authentication.
     * @return Return a JSON string of all scraped events.
     */
    public static Result scrape_graph() {
        JsonObject result = facebook_graph_query(USER_ACCESS_TOKEN, "*", GRAPH_SEARCH_TYPE, 
            GRAPH_EVENT_FIELDS, CENTER_LATITUDE + "," + CENTER_LONGITUDE, CENTER_RADIUS, GRAPH_LIMIT);
        JsonArray events = result.getJsonArray("data");
        List<MyEvent> eventList = save_events(events);
        return ok( JSONValue.toJSONString(eventList) );
    }

    /**
     * Scrapes events from Facebook using the Facebook Query Language (FQL). It searches
     * for events using the defined FQL_QUERY.
     * <p> 
     * The FQL_QUERY searches for events that are created by Facebook Pages.
     * It filters these events based on pages whose location is within CENTER_RADIUS
     * meters from (CENTER_LATITUDE, CENTER_LONGITUDE).
     * <p>
     * As stated above, this query only searches for events that are created by a page.
     * The event_member table in FQL contains uids and page_ids of, as the name suggests, 
     * members of events. The logic behind the query is that if a page is a member of an
     * event, the page is the creator the event.
     * Most pages have a location associated with them. If the location is within 
     * CENTER_RADIUS meters from (CENTER_LATITUDE, CENTER_LONGITUDE) the event will be
     * returned.
     * <p>
     * However, one major pitfall is international/national organizations that have their
     * headquarters within this radius. The page resides here, but the actual event
     * may be in Washinton, DC or another country. Some examples of these are SAE and
     * Rotary International. There is no obvious solution for filtering with these events,
     * especially if they don't have a listed venue. The filter_events function in Utilities.java
     * explores this in greater detail.
     * @return Return a JSON string of all scraped events.
     */
    public static Result scrape_fql() {

        String FQL_DISTANCE_QUERY = "distance(latitude, longitude, '" + 
            CENTER_LATITUDE + "', '" + CENTER_LONGITUDE + "') " + "< " + CENTER_RADIUS;

        String FQL_QUERY =
            "SELECT " + FQL_EVENT_FIELDS + " FROM " + FQL_EVENT_TABLE + " WHERE eid IN " +
                    "(SELECT " + FQL_EVENT_MEMBER_FIELDS + " FROM " + 
                        FQL_EVENT_MEMBER_TABLE + " WHERE uid IN " +
                            "(SELECT " + FQL_PLACE_FIELDS + " FROM " + 
                                    FQL_PLACE_TABLE + " WHERE " + FQL_DISTANCE_QUERY + " " + 
                            FQL_LIMIT + ") " +
                    FQL_LIMIT + ") " +
            FQL_ORDER + " " + FQL_LIMIT;

        List<JsonObject> events = facebook_fql_query(APP_ACCESS_TOKEN, FQL_QUERY);
        List<MyEvent> eventList = save_events(events);

        return ok( JSONValue.toJSONString(eventList) );
    }

    /**
     * Scrapes organizations using seed data in the MyOrganization model. It searches for events
     * using an FQL query that finds all events created by a specific organization.
     * <p>
     * For some reason, the majority (if not all) of events found using this method are from
     * past years. However, the locations, venues, and other data that result are valuable
     * for other functions.
     * @return Return a JSON string of all scraped events.
     */
    public static Result scrape_organizations() {

        String FQL_ORGANIZATIONS_QUERY = "SELECT " + FQL_EVENT_FIELDS + " FROM " + 
                                            FQL_EVENT_TABLE + " WHERE creator = ";

        List<MyEvent> organizationEvents = new ArrayList<MyEvent>();
        List<MyOrganization> organizations = MyOrganization.find.all();

        for(MyOrganization organization:organizations) {
            List<JsonObject> events = facebook_fql_query(APP_ACCESS_TOKEN, 
                FQL_ORGANIZATIONS_QUERY + organization.fbid + " " + FQL_LIMIT);
            List<MyEvent> eventList = save_events(events);
            organizationEvents.addAll(eventList);
        }

        return ok( JSONValue.toJSONString(organizationEvents) );
    }

    /**
     * Scrape Locations uses data already in the MyEvent table to search for events.
     * It uses the getAllEventLocations function in MyEvent.java to find a list of all
     * locations where events currently reside based on the events in the database.
     * It then uses the Facebook Graph API to perform a text based query of these
     * locations. This is a powerful function and grabs a myriad of events. However,
     * filtering is extremly important as the majority of these events will be outside
     * of the city/state/country.
     * @return Return a JSON string of all scraped events.
     */
     public static Result scrape_locations() {
        List<MyEvent> locationEvents = new ArrayList<MyEvent>();
        ArrayList<String> locations = MyEvent.getAllEventLocations();
        for (int i = 0; i < locations.size(); i++) {
            JsonObject result = facebook_graph_query(APP_ACCESS_TOKEN, locations.get(i), 
                GRAPH_SEARCH_TYPE, GRAPH_EVENT_FIELDS, "", "", GRAPH_LIMIT);
            JsonArray single_location_events = result.getJsonArray("data");
            List<MyEvent> eventList = save_events(single_location_events);
            locationEvents.addAll(eventList);
        }

        return ok( JSONValue.toJSONString(locationEvents) );
    }

}