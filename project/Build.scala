import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "nuPlanner"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    jdbc,
    "org.jsoup" % "jsoup" % "1.7.2", // doesn't have support for JSON, thus must use GSON
    "com.google.code.gson" % "gson" % "2.2.4", // used for parsing JSON
    "postgresql" % "postgresql" % "9.1-901.jdbc4", // postgres database for heroku
    "com.restfb" % "restfb" % "1.6.12", // used for accessing facebook API (also has json support)
    "com.gistlabs" % "mechanize" % "0.11.0", // unecessary if jsoup is being used (supports HTML, JSON, and XML)
    "commons-io" % "commons-io" % "2.3"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    ebeanEnabled := true
  )

}
