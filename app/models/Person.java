package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@MappedSuperclass
public class Person extends Model {
    
    @Id public Long id;
    @Constraints.Required public String name;
    public Person(String name) { 
        this.name = name;
        System.out.println("person constructor");
    }
    
    // public static Finder<String,Person> find = new Finder<String,Person>( String.class, Person.class );
    // public static Person findByName(String name) { return find.where().eq("name", name); }
    // List<Person> users = Person.find.all();
 
}