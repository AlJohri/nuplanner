package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Student extends Person {
    public Student(String name) { 
        super(name);
    }
    public static Finder<String,Student> find = new Finder<String,Student>( String.class, Student.class );
}