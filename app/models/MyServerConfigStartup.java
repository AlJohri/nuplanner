// http://www.playframework.com/documentation/2.1.1/JavaEbean (Configuring Ebean)

// package models;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.event.ServerConfigStartup;
import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebean.config.dbplatform.*;

// http://jamestyrrell.github.io/2013/02/23/Ebean-Postgres-And-Identity/
public class MyServerConfigStartup implements ServerConfigStartup {
    @Override
    public void onStart(ServerConfig config) {
        // final ServerConfig config = new ServerConfig();
        final PostgresPlatform postgresPlatform = new PostgresPlatform();
        postgresPlatform.getDbIdentity().setIdType(IdType.IDENTITY);
        postgresPlatform.getDbIdentity().setSupportsIdentity(true);
        config.setDatabasePlatform(postgresPlatform);
        // serverConfig.add(new BeanPersistListener() { });
    }
}