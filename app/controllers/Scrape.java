package controllers;

/* General Java */
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

/* Play Frameowork */
import play.*;
import play.mvc.*;
import play.data.*;
import play.db.ebean.*;
import models.*;
import views.html.*;

/**
 * This class serves as a wrapper for ScrapeFacebook and ScrapeWildcatConnection classes.
 * @author Al Johri
 * @version 0.1
 */
public class Scrape extends Controller {

	/**
	 * This method runs smaller scrape methods from the Scrape classes's children
	 * in the necessary order to get the best final results.
	 * <p>
	 * ScrapeFacebook.scrape_locations() must be run last because it uses previously
	 * scraped events to generate its queries.
	 * <p>
	 * This controller is called by the /scrape url 
	 * @return Return success message.
	 */
    public static Result scrape() {
        Result fql = ScrapeFacebook.scrape_fql();
        Result organizations = ScrapeFacebook.scrape_organizations();
        Result locations = ScrapeFacebook.scrape_locations();
        // Result graph = ScrapeFacebook.scrape_graph();
        // Result wildcat_connection = ScrapeWildcatConnection.scrape_wildcatconnection();

        return ok("done scraping!");
    }

}
