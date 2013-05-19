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


//http://www.java-tips.org/java-ee-tips/enterprise-java-beans/inheritance-and-the-java-persistenc.html
// @Inheritance(strategy=InheritanceStrategy.SINGLE_TABLE)
// @DiscriminatorColumn(name="DTYPE", discriminatorType=DiscriminatorType.STRING)
// @DiscriminatorValue("Person");
