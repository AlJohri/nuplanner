package models;

import java.util.*;
import javax.persistence.*;

// http://www.avaje.org/static/javadoc/pub/
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import org.joda.time.DateTime;

@Entity
public class MyEvent extends Model {
    @Id public Long eid;
    @Constraints.Required public String name;
    @Constraints.Required public String creator;
    
    public String location;
    public String venue;

    public DateTime start_time;
    public DateTime end_time;

    @Column(columnDefinition = "TEXT")
    @Constraints.Required public String description;

    public MyEvent(Long eid, String name,String creator, DateTime start_time, DateTime end_time, String location, String venue, String description) { 
        this.eid = eid;
        this.name = name;
        this.creator = creator;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.venue = venue;
        this.description = description;
    }

    public static Finder<String,MyEvent> find = new Finder<String,MyEvent>( String.class, MyEvent.class );
    public static Finder<Long,MyEvent> find2 = new Finder<Long,MyEvent>( Long.class, MyEvent.class );
}

/*
    protected String time;
    protected String place;
    protected String organization;
    protected String owner;
    protected String description;

 */