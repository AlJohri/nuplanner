package models;

import java.util.*;
import javax.persistence.*;
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

        String jeid = String.valueOf(eid);
        String jtitle = JSONObject.escape(name);
        String jcreator = JSONObject.escape(creator);
        String start = (start_time != null) ? String.valueOf(start_time.getMillis()/1000) : "";        
        String end = (end_time != null) ? String.valueOf(end_time.getMillis()/1000) : "";   
        String jdescription = JSONObject.escape(description);
        String jpic = JSONObject.escape(pic);
        String jurl = JSONObject.escape("http://www.facebook.com/events/") + String.valueOf(eid);
        String jclassname = JSONObject.escape("");

        sb.append("{");

            sb.append("\"" + "id" + "\""); sb.append(":"); sb.append("\"" + jeid + "\""); sb.append(",");
            sb.append("\"" + "title" + "\""); sb.append(":"); sb.append("\"" + jtitle + "\""); sb.append(",");
            sb.append("\"" + "allDay" + "\""); sb.append(":"); sb.append("false"); sb.append(",");
            sb.append("\"" + "creator" + "\""); sb.append(":"); sb.append("\"" + jcreator + "\""); sb.append(",");
            sb.append("\"" + "start" + "\""); sb.append(":"); sb.append("\"" + start + "\""); sb.append(",");
            sb.append("\"" + "end" + "\""); sb.append(":"); sb.append("\"" + end + "\""); sb.append(",");
            sb.append("\"" + "description" + "\""); sb.append(":"); sb.append("\"" + jdescription + "\""); sb.append(",");
            sb.append("\"" + "pic" + "\""); sb.append(":"); sb.append("\"" + jpic + "\""); sb.append(",");
            sb.append("\"" + "url" + "\""); sb.append(":"); sb.append("\"" + jurl + "\""); sb.append(",");
            sb.append("\"" + "className" + "\""); sb.append(":"); sb.append("\"" + jclassname + "\""); sb.append(",");
            sb.append("\"" + "editable" + "\""); sb.append(":"); sb.append("false");

        sb.append("}");

        return sb.toString();
    }

    public static ArrayList getAllEventLocations() {
        ArrayList<String> locations = new ArrayList<String>();
        List<MyEvent> events = MyEvent.find.all();
        for (MyEvent event:events) { 
            if (event.location != null && !event.location.isEmpty()) {
                locations.add(event.location);    
            }
        }
        return locations;
    }

    // http://www.avaje.org/static/javadoc/pub/com/avaje/ebean/ExpressionList.html
    public static Finder<String,MyEvent> find = new Finder<String,MyEvent>( String.class, MyEvent.class );
    public static Finder<Long,MyEvent> findLong = new Finder<Long,MyEvent>( Long.class, MyEvent.class );
    public static Finder<DateTime,MyEvent> findDate = new Finder<DateTime,MyEvent>( DateTime.class, MyEvent.class );
}
