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
    "org.jsoup" % "jsoup" % "1.7.2",
    "com.google.code.gson" % "gson" % "2.2.4", 
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.restfb" % "restfb" % "1.6.12",
    "com.gistlabs" % "mechanize" % "0.11.0",
    "commons-io" % "commons-io" % "2.3"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    ebeanEnabled := true
  )

}
