package controllers;

import java.util.*;

/* RestFB */
import com.restfb.*;
import com.restfb.FacebookClient.*;
import com.restfb.json.*;
import com.restfb.types.Event;

/**
 * The Facebook class contains ACCESS_TOKENS, FQL query fragments, graph query fragments, and 
 * helper functions to encapsulate the RestFB query methods.
 * @author Al Johri
 */
public final class Facebook {

    /* Facebook App Access Token */
    public static final String APP_ACCESS_TOKEN = "524073037656113|l1aTC3FhsPHJEeRZfWB9vk70nAk";
    public static final String USER_ACCESS_TOKEN = "CAAHcpEMZB8DEBANQkZAyhJCXEYnAn2UW9paEZC7VWGJCb9a1ouGismLu8xKfCZCZBdtAl3Ld4iVC6u2HaXHqmknogy4RMuMswUMqc17PKSwAt4UmQoqDT4RIHKQaeHpnoXjXkCWTFlx7fNKKvDLUaauyS3KIuFsvziJaCphDuCgZDZD";    

    /* Coordinate Constants */
    public static final String CENTER_LATITUDE = "42.054581";
    public static final String CENTER_LONGITUDE = "-87.677192";
    public static final String CENTER_RADIUS = "1000";    

    /* FQL Fields */
    public static final String FQL_EVENT_FIELDS = "eid, name, creator, host, start_time, end_time, location, venue, description, pic, pic_big, pic_cover, pic_small, pic_square, timezone, update_time, all_members_count, attending_count, declined_count, unsure_count, not_replied_count";
    public static final String FQL_EVENT_MEMBER_FIELDS = "eid";
    public static final String FQL_PLACE_FIELDS = "page_id";

    /* FQL Tables */
    public static final String FQL_EVENT_TABLE = "event";
    public static final String FQL_EVENT_MEMBER_TABLE = "event_member";
    public static final String FQL_PLACE_TABLE = "place";

    /*FQL LIMIT and ORDER */
    public static final String FQL_LIMIT = "LIMIT 51000";
    public static final String FQL_ORDER = "ORDER BY start_time ASC";

    /* Graph Fields */
    public static final String GRAPH_EVENT_FIELDS = "id,name,owner,description,start_time,end_time,location,venue,privacy,updated_time,picture,ticket_uri";
    public static final String GRAPH_SEARCH_TYPE = "event";
    public static final String GRAPH_LIMIT = "5000";

    /* Blocked Organizations */
    public static final String[] BLOCKED_ORGANIZATIONS = new String[] {
        "7268844551",       // Rotary International
        "294833066685",
        "163623007048538",
        "37873197144",
        "331587493531436",
        "410474245699160",
        "18019562194",
        "271418392986638"
    };

    /* Blocked Venues */
    public static final String[] BLOCKED_VENUES = new String[] {
        "17070917171",      // Actors Gymnasium
        "356690111071371",  // 27 Live
        "430645200319091",  // World of Beer - Evanston
        "265779566799135",  // Chicago Northshore Kinetics
        "120569982856",     // Harvest Mission Community Church of Chicago
        "155288914484855",  // Lululemon Athletica Evanston
        "161587597298994",  // Gather
        "132449680200187",  // The Smokehouse
        "253108394915",     // Kapnick Business Institutions
        "110184922344060",  // Washington, District of Columbia
        "115288991957781",  // Chemistry of Life Processes Institute (CLP)
        "143834612313652",  // Wilmette Golf Club
        "111268432255671",  // Grateful Yoga of Evanston
        "44661328399",      // Evanston Art Center
        "442292685838282",  // Feast & Imbibe
        "110432202311659",  // Lisbon, Portugal
        "115421351813697",  // Lake Street Church of Evanston
        "143964878955940",  // Lollie evanston
        "252037288213690",  // Ida Noyes Hall at the University of Chicago
        "17179183149",      // The Art Institute of Chicago
        "43871522761",
        "35114506738",
        "109977289040245"   // Wrigley Field

    };

    /**
     * [facebook_fql_query description]
     * @param  token [description]
     * @param  query [description]
     * @return       [description]
     */
    public static List<com.restfb.json.JsonObject> facebook_fql_query(String token, String query) {
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        return facebookClient.executeFqlQuery(query, com.restfb.json.JsonObject.class);
    }

    /**
     * [facebook_graph_query description]
     * @param  token    [description]
     * @param  q        [description]
     * @param  type     [description]
     * @param  fields   [description]
     * @param  center   [description]
     * @param  distance [description]
     * @param  limit    [description]
     * @return          [description]
     */
    public static com.restfb.json.JsonObject facebook_graph_query(String token, String q, String type, String fields, String center, String distance, String limit) {
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        return facebookClient.fetchObject( "search", com.restfb.json.JsonObject.class,
            Parameter.with("q", q),
            Parameter.with("type", type),
            Parameter.with("fields", fields),
            Parameter.with("center", center),
            Parameter.with("distance", distance),
            Parameter.with("limit", limit)
        );
    }
        
}