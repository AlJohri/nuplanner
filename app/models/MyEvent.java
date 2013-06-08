package models;

import java.util.*;
import javax.persistence.*;

// http://www.avaje.org/static/javadoc/pub/
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import org.joda.time.DateTime;

import org.json.*;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.JSONAware;

@Entity
public class MyEvent extends Model implements JSONAware {
    @Id public Long eid;
    @Constraints.Required public String name;
    @Constraints.Required public String creator;

    public String location;
    public String venue;

    public DateTime start_time;
    public DateTime end_time;
	
	public String pic;
	
    @Column(columnDefinition = "TEXT")
    @Constraints.Required public String description;

    public MyEvent(Long eid, String name,String creator, DateTime start_time, DateTime end_time, String location, String venue, String description, String pic) {
        this.eid = eid;
        this.name = name;
        this.creator = creator;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.venue = venue;
        this.description = description;
		this.pic = pic;
    }

    public String toJSONString(){
        StringBuffer sb = new StringBuffer();

        sb.append("{");

        sb.append("\"" + JSONObject.escape("id") + "\"");
        sb.append(":");
        sb.append("\"" + String.valueOf(eid) + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("title") + "\"");
        sb.append(":");
        sb.append("\"" + JSONObject.escape(name) + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("allDay") + "\"");
        sb.append(":");
        sb.append("false");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("creator") + "\"");
        sb.append(":");
        sb.append("\"" + JSONObject.escape(creator) + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("start") + "\"");
        sb.append(":");
        String start = (start_time != null) ? String.valueOf(start_time.getMillis()/1000) : "";
        sb.append("\"" + start + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("end") + "\"");
        sb.append(":");
        String end = (end_time != null) ? String.valueOf(end_time.getMillis()/1000) : "";
        sb.append("\"" + end + "\"");
		
        sb.append(",");

        sb.append("\"" + JSONObject.escape("description") + "\"");
        sb.append(":");
        sb.append("\"" + JSONObject.escape(description) + "\"");
		
        sb.append(",");

        sb.append("\"" + JSONObject.escape("pic") + "\"");
        sb.append(":");
        sb.append("\"" + JSONObject.escape(pic) + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("url") + "\"");
        sb.append(":");
        sb.append("\"" + JSONObject.escape("http://www.facebook.com/events/") + String.valueOf(eid) + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("className") + "\"");
        sb.append(":");
        sb.append("\"" + JSONObject.escape("") + "\"");

        sb.append(",");

        sb.append("\"" + JSONObject.escape("editable") + "\"");
        sb.append(":");
        sb.append("false");

        sb.append("}");

        return sb.toString();
    }

    // http://www.avaje.org/static/javadoc/pub/com/avaje/ebean/ExpressionList.html
    public static Finder<String,MyEvent> find = new Finder<String,MyEvent>( String.class, MyEvent.class );
    public static Finder<Long,MyEvent> findLong = new Finder<Long,MyEvent>( Long.class, MyEvent.class );
    public static Finder<DateTime,MyEvent> findDate = new Finder<DateTime,MyEvent>( DateTime.class, MyEvent.class );
}
