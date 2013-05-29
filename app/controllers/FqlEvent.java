package controllers;




import com.restfb.types.*;

import com.restfb.Facebook;

public class FqlEvent {
		  @Facebook
		  Long eid;
		  
		  @Facebook
		  String name;
		  
		  @Facebook
		  String creator;
		  
		  @Facebook
		  String start_time;
		  
		  @Facebook
		  String end_time;
		  
		  @Facebook
		  String location;
		  
		  @Facebook
		  String pic_big;
		  
		  @Facebook
		  String description;
		  @Override
		  public String toString() {
		    return String.format("%s (%d)", name, eid);
		  }
		}
