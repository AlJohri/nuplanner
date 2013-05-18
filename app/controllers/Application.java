package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import models.*;
import play.data.*;

public class Application extends Controller {
  
    public static Result index() {
        return redirect(routes.Application.test());
    }

	public static Result persons() {
	  return ok( "lala"
	    // views.html.index.render(User.all())
	  );
	}

	public static Result test() {
		new Person("Bob The Builder").save();
		return ok( "lala"
			// views.html.index.render(User.all())
		);
	}	

}
