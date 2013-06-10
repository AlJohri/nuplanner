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

public class Scrape extends Controller {

    public static Result scrape() {
        Result fql = ScrapeFacebook.scrape_fql();
        Result organizations = ScrapeFacebook.scrape_organizations();
        Result locations = ScrapeFacebook.scrape_locations();

        return ok("done scraping!");
    }

}
