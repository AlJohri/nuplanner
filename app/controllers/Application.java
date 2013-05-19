package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import models.*;
import play.data.*;

import com.restfb.*; // http://restfb.com/javadoc/
import com.restfb.json.JsonObject;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonTokener;
import com.restfb.FacebookClient.*;
import com.restfb.types.Event;

import java.util.*;

public class Application extends Controller {
  
    public static Result index() {
        return redirect(routes.Application.events());
    }

	public static Result students() {
	  return ok( "lala" /* views.html.index.render(User.all()) */ );
	}

	public static Result events() {
		String MY_ACCESS_TOKEN = "CAACEdEose0cBAOBMsL2Cz1dzKqQv1QBA1zE5vl6wFziPMBZA4eKKSJCvJnzdkCEZBC8nlgacUt95XSDYtdhcVUiC76Xnpo6ea64KqiMeyUuXPp34EBh683JBfjIBO44nef0YK3NnjsyxlA2pLVT7y797AMRVwZD";
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);

		// Loop through connection object : https://groups.google.com/d/msg/restfb/eHMSgUxEXi4/gemE6_meNyAJ
		Connection<JsonObject> connection = facebookClient.fetchConnection( 
			"search", JsonObject.class, 
			Parameter.with("q", "*"), 
			Parameter.with("center", "42.052925,-87.665834"), 
			Parameter.with("type", "event"),
			Parameter.with("distance", 1000)
		);

		JsonArray events = new JsonArray();
		do {
			events.put(connection.getData());
			connection = facebookClient.fetchConnectionPage(connection.getNextPageUrl(), JsonObject.class);
		} while (connection.hasNext());

		System.out.println(events.toString());

		return ok( events.toString() );
	}

}