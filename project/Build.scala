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
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.restfb" % "restfb" % "1.6.12",
    "com.gistlabs" % "mechanize" % "0.11.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    ebeanEnabled := true
  )

}