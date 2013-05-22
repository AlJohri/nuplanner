package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import org.joda.time.DateTime;

@Entity
public class Event extends Model {
    @Id public Long id;
    @Constraints.Required public String name;
    @Constraints.Required public String creator;
    
    public String location;
    public String venue;

    public DateTime start_time;
    public DateTime end_time;

    @Column(columnDefinition = "TEXT")
    @Constraints.Required public String description;    

    public Event(String name,String creator, DateTime start_time, DateTime end_time, String location, String venue, String description) { 
        this.name = name;
        this.creator = creator;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.venue = venue;
        this.description = description;
    }

    public static Finder<String,Event> find = new Finder<String,Event>( String.class, Event.class );
}

/*
    protected String time;
    protected String place;
    protected String organization;
    protected String owner;
    protected String description;

 */