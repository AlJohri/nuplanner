package controllers;

/* RestFB */
import com.restfb.*;
import com.restfb.FacebookClient.*;
import com.restfb.json.*;
import com.restfb.types.Event;

public final class FB {  

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
    // FQL LIMIT and ORDER
    public static final String FQL_LIMIT = "LIMIT 51000";
    public static final String FQL_ORDER = "ORDER BY start_time ASC";

    public static final String GRAPH_EVENT_FIELDS = "id,name,owner,description,start_time,end_time,location,venue,privacy,updated_time,picture,ticket_uri";
    public static final String GRAPH_SEARCH_TYPE = "event";
    public static final String GRAPH_LIMIT = "5000";

    public static final String[] BLOCKED_ORGANIZATIONS = new String[] {
        "7268844551"
    };

    public static final String[] BLOCKED_VENUES = new String[] {
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
        "115421351813697",
        "143964878955940",
        "252037288213690"
    };

    public static List<com.restfb.json.JsonObject> facebook_fql_query(String token, String query) {
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        return facebookClient.executeFqlQuery(query, com.restfb.json.JsonObject.class);
    }

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