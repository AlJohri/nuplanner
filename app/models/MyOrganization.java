package models;
//json
import org.json.*;

import com.google.gson.*;
//


import java.util.*;
import javax.persistence.*;

// http://www.avaje.org/static/javadoc/pub/
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import org.joda.time.DateTime;

@Entity
public class MyOrganization extends Model {
    @Id public Long fbid;
    @Constraints.Required public String name;
    public String creator;
    public String url;
    public String location;

    @Column(columnDefinition = "TEXT")
    @Constraints.Required public String description;

    public MyOrganization(Long fbid, String name,String location, String url, String description) { 
        this.fbid = fbid;
        this.name = name;
        this.location=location;
        this.url = url;
        this.description = description;
    }
    public String getJsonObject(){
    	MyOrganization obj = new MyOrganization(this.fbid,this.name,this.creator,this.url,this.description);
    	Gson gson = new Gson();
    	String json = gson.toJson(obj);
    	return json;
    }
    // http://www.avaje.org/static/javadoc/pub/com/avaje/ebean/ExpressionList.html
    public static Finder<String,MyOrganization> find = new Finder<String,MyOrganization>( String.class, MyOrganization.class );
    public static Finder<Long,MyOrganization> findLong = new Finder<Long,MyOrganization>( Long.class, MyOrganization.class );
 
}