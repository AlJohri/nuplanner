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

    public static Result scrape_graph() {
        String USER_TOKEN = "CAAHcpEMZB8DEBANQkZAyhJCXEYnAn2UW9paEZC7VWGJCb9a1ouGismLu8xKfCZCZBdtAl3Ld4iVC6u2HaXHqmknogy4RMuMswUMqc17PKSwAt4UmQoqDT4RIHKQaeHpnoXjXkCWTFlx7fNKKvDLUaauyS3KIuFsvziJaCphDuCgZDZD";
        JsonObject result = facebook_graph_query(USER_TOKEN, "*", GRAPH_SEARCH_TYPE, GRAPH_EVENT_FIELDS, CENTER_LATITUDE + "," + CENTER_LONGITUDE, CENTER_RADIUS, GRAPH_LIMIT);
        JsonArray events = result.getJsonArray("data");
        return ok(events.toString());
    }

    public static Result scrape_fql() {
        List<JsonObject> events = facebook_fql_query(ACCESS_TOKEN, FQL_QUERY);
        List<MyEvent> eventList = save_events(events);

        return ok( JSONValue.toJSONString(eventList) );
    }
 
     public static Result scrape_locations() {
        List<MyEvent> locationEvents = new ArrayList<MyEvent>();
        ArrayList<String> locations = getLocations();
        for (int i = 0; i < locations.size(); i++) {
            JsonObject result = facebook_graph_query(ACCESS_TOKEN, locations.get(i), GRAPH_SEARCH_TYPE, GRAPH_EVENT_FIELDS, "", "", GRAPH_LIMIT);
            JsonArray single_location_events = result.getJsonArray("data");
            List<MyEvent> eventList = save_events(single_location_events);
            locationEvents.addAll(eventList);
        }

        return ok( JSONValue.toJSONString(locationEvents) );
    }

    public static Result scrape_organizations() {
        List<MyEvent> organizationEvents = new ArrayList<MyEvent>();
        List<MyOrganization> organizations = MyOrganization.find.all();

        for(MyOrganization organization:organizations) {
            List<JsonObject> events = facebook_fql_query(ACCESS_TOKEN, FQL_ORGANIZATIONS_QUERY + organization.fbid + " " + FQL_LIMIT);
            List<MyEvent> eventList = save_events(events);
            organizationEvents.addAll(eventList);
        }

        return ok( JSONValue.toJSONString(organizationEvents) );
    }

}