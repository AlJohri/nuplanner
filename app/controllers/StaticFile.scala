package controllers

import views._
import java.io._
import play.Logger
import play.api._
import play.api.libs.ws._
import play.api.libs.oauth._
import play.api.mvc._
import play.api.libs._

object StaticFile extends Controller {

    def html(file: String) = Action {
      var f = new File(file)

      if (f.exists())
        Ok(scala.io.Source.fromFile(f.getCanonicalPath()).mkString).as("text/html");
      else
        NotFound
    }

}