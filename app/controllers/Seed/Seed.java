package controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import play.*;
import play.mvc.*;
import play.data.*;
import play.db.ebean.*;
import models.*;
import views.html.*;
import org.joda.time.format.*;
import org.joda.time.DateTime;
import org.jsoup.*;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.*;
import org.json.simple.JSONValue;
import com.google.gson.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The Seed class collects data from various sources on the web that will be saved to the database
 * in order to aid scrapers.
 * @author Sai Praneeth
*/

public class Seed extends Controller {

	/**
	 * Calls all seed methods. Currently just seed_organizations().
     * <p>
	 * This controller is called by the /seed url
	 * @return Return success message.
	 */
    public static Result seed() {
        seed_organizations();
        
        return ok("Seed complete!");
    }

    /**
     * Get list of buildings at Northwestern from maps.northwestern.edu.
     * Evanston: http://maps.northwestern.edu/dialog/building_list/Evanston/
     * Chicago: http://maps.northwestern.edu/dialog/building_list/Chicago/
     * TODO: implement this function
     * @return Return success message.
     */
    public static Result seed_locations() {
        return ok("done");
    }
    
	/**
	 * Get html pages for all organizations from northwestern collegiatelink
	 * Parse these pages to get their facebook id 
	 * Add these id's to graph url to get their facebook info
	 * Parse this info to get necessary details of that organization
	 * Save this info in database into My Organization table
	 * @return  Return success message.
	 */
    public static Result seed_organizations() {
    	String a="";
        for (int i=1;i<52;i++) {
            Document doc;
            Document doc1;
            JSONObject json;
            try {
                String[] name =  new String[100];
                String[] name1 = new String[100];
                String[] name2 = new String[100];
                int[] id =new int[100];
                doc = Jsoup.connect("https://northwestern.collegiatelink.net/organizations?SearchType=None&SelectedCategoryId=0&CurrentPage="+ i).get();
                Element sa = doc.getElementById("results");
                Elements sai = sa.getElementsByTag("h5");
                int j=0;
                for(Element e:sai) {
                    Element f = e.child(0);
                    Document doc2=Jsoup.connect("https://northwestern.collegiatelink.net" + f.attr("href")).get();
                    Elements fb=doc2.select("a[class*=icon-social facebook]");
                    for (Element g:fb) {
                        if((g.attr("href").length()!=4)) {
                            name[j]=g.attr("href");//facebook link
                            id[j]=name[j].lastIndexOf("/");
                            name1[j]=name[j].substring(id[j]+1);//last string after / and groups end with a / in fb link
                            if (!name1[j].isEmpty()) {
                                name2[j]="https://graph.facebook.com/"+name1[j];//graph url
                                System.out.println(name2[j]);
                                InputStream is =new URL(name2[j]).openStream();
                                try {
                                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                                    StringBuilder sb = new StringBuilder();
                                    int cp;
                                    while((cp=rd.read())!=-1)
                                        sb.append((char)cp);
                                    String jsontext=sb.toString();
                                    try {
                                        json = new JSONObject(jsontext);
                                         a=json.get("id").toString();
                                         Long fbid = Long.valueOf(a).longValue();
                                         String fbname = json.has("name") ? json.get("name").toString() : "";
                                         String location = json.has("location") ? json.get("location").toString() : "";
                                         String link = json.has("link") ? json.get("link").toString() : "";
                                         String description = json.has("description") ? json.get("description").toString() : "";
                                         if (MyOrganization.findLong.byId(fbid) == null) new MyOrganization(fbid, fbname, location, link, description).save();
                                    } catch (JSONException e1) {
                                        System.out.println(e1.getMessage());
                                    }
                                }
                                finally { 
                                    is.close(); 
                                }
                                j++;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return ok("done");
    }
}
