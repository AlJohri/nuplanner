import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

import models.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */   
    // @Test
    // public void test() {
    //     running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
    //         public void invoke(TestBrowser browser) {
    //             browser.goTo("http://localhost:3333");
    //             assertThat(browser.pageSource()).contains("lala");
    //         }
    //     });
    // }

    @Test
    public void createAndRetrieveUser() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                new Person("Bob The Builder").save();
                Person bob = Person.find.where().eq("name", "Bob The Builder").findUnique(); //.first();
                System.out.println(bob.name);
                assertThat(bob).isNotNull();
                assertThat(bob.name).contains("Bob The Builder");
            }
        });

    }    
  
}
