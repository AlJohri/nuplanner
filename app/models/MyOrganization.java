package models;

import org.json.*;
import com.google.gson.*;
import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import org.joda.time.DateTime;
/**
 * Class for Organizations
 * @author SaiPraneeth
 *
 */
@Entity
public class MyOrganization extends Model {
	/**
	 * Organization Facebook Id
	 */
    @Id public Long fbid;
    /**
     * Organization name
     */
    @Constraints.Required public String name;
    /**
     * Organization Creator
     */
    public String creator;
    /**
     * Organization facebook url
     */
    public String url;
    /**
     * Organization location
     */
    public String location;

    @Column(columnDefinition = "TEXT")
    /**
     * Organization description
     */
    @Constraints.Required public String description;
    /**
     * Organization constructor
     */
    public MyOrganization(Long fbid, String name,String location, String url, String description) { 
        this.fbid = fbid;
        this.name = name;
        this.location = location;
        this.url = url;
        this.description = description;
    }
    /**
     * Convert json object to string
     * @return string
     */
    public String getJsonObject(){
        MyOrganization obj = new MyOrganization(this.fbid,this.name,this.creator,this.url,this.description);
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }
    /**
     * For finding organizations based on parameter
     */
    public static Finder<String,MyOrganization> find = new Finder<String,MyOrganization>( String.class, MyOrganization.class );
    public static Finder<Long,MyOrganization> findLong = new Finder<Long,MyOrganization>( Long.class, MyOrganization.class );
 
}