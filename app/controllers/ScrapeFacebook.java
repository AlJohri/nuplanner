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

/* DateTime */
import org.joda.time.format.*;
import org.joda.time.DateTime;

/* JSON */
import org.json.simple.JSONValue;

/* RestFB */
import com.restfb.*;
import com.restfb.FacebookClient.*;
import com.restfb.json.*;
import com.restfb.types.Event;

/* Facebook Constants */
import static controllers.FB.*;
import static controllers.Utilities.*;

public class ScrapeFacebook extends Controller {

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
     * for events using an FQL_QUERY defined in FB.java. This query 
     * @return [description]
     */
    public static Result scrape_fql() {

        String FQL_DISTANCE_QUERY = "distance(latitude, longitude, '" + 
            CENTER_LATITUDE + "', '" + CENTER_LONGITUDE + "') " + "< " + CENTER_RADIUS;

        String FQL_QUERY =
            "SELECT " + FQL_EVENT_FIELDS + " FROM " + FQL_EVENT_TABLE + " WHERE eid IN " +
                    "(SELECT " + FQL_EVENT_MEMBER_FIELDS + " FROM " + FQL_EVENT_MEMBER_TABLE + " WHERE uid IN " +
                                            "(SELECT " + FQL_PLACE_FIELDS + " FROM " + 
                                                         FQL_PLACE_TABLE + " WHERE " + FQL_DISTANCE_QUERY + " " + 
                                             FQL_LIMIT + ") " +
                            FQL_LIMIT + ") " +
            FQL_ORDER + " " + FQL_LIMIT;

        List<JsonObject> events = facebook_fql_query(APP_ACCESS_TOKEN, FQL_QUERY);
        List<MyEvent> eventList = save_events(events);

        return ok( JSONValue.toJSONString(eventList) );
    }
 
     public static Result scrape_locations() {
        List<MyEvent> locationEvents = new ArrayList<MyEvent>();
        ArrayList<String> locations = getLocations();
        for (int i = 0; i < locations.size(); i++) {
            JsonObject result = facebook_graph_query(APP_ACCESS_TOKEN, locations.get(i), 
                GRAPH_SEARCH_TYPE, GRAPH_EVENT_FIELDS, "", "", GRAPH_LIMIT);
            JsonArray single_location_events = result.getJsonArray("data");
            List<MyEvent> eventList = save_events(single_location_events);
            locationEvents.addAll(eventList);
        }

        return ok( JSONValue.toJSONString(locationEvents) );
    }

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

}