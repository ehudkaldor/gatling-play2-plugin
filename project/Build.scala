import sbt._
import Keys._

object MinimalBuild extends Build {

  val appName = "gatling-play2-plugin"
  val buildVersion =  "1.0-SNAPSHOT"

  lazy val typesafeSnapshot = "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  lazy val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

  /* OFFICIAL GATLING REPO */
  val gatlingReleasesRepo = "Gatling Releases Repo" at "http://repository.excilys.com/content/repositories/releases"
  val gatling3PartyRepo = "Gatling Third-Party Repo" at "http://repository.excilys.com/content/repositories/thirdparty"

  /* LOCAL MAVEN REPO */
  val localMavenRepo = "Local Maven Repository" at file(Path.userHome.absolutePath+"/.m2/repository").toURI.toURL.toString

  /* GATLING DEPS */
  val gatlingVersionNumber = "1.1.4"
  val gatlingApp = "com.excilys.ebi.gatling" % "gatling-app" % gatlingVersionNumber //withSources
  val gatlingCore = "com.excilys.ebi.gatling" % "gatling-core" % gatlingVersionNumber //withSources
  val gatlingHttp = "com.excilys.ebi.gatling" % "gatling-http" % gatlingVersionNumber //withSources
  val gatlingRecorder = "com.excilys.ebi.gatling" % "gatling-recorder" % gatlingVersionNumber //withSources
  val gatlingCharts = "com.excilys.ebi.gatling" % "gatling-charts" % gatlingVersionNumber //withSources
  val gatlingHighcharts = "com.excilys.ebi.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersionNumber //withSources


  val specs2 = "org.specs2" %% "specs2" % "1.8.2" withSources

  val libDependencies = Seq(
    "play" %% "play" % "2.0",

     gatlingApp,
     gatlingCore,
     gatlingHttp,
     gatlingRecorder,
     gatlingCharts,
     gatlingHighcharts,

    specs2,

    "play" %% "play-test" % "2.0"
  )



  lazy val root = Project(id = appName, base = file("."), settings = Project.defaultSettings).settings(
    version := buildVersion,
    organization := "be.nextlab",
    resolvers ++= Seq(typesafe, typesafeSnapshot, gatlingReleasesRepo, gatling3PartyRepo, localMavenRepo),
    javacOptions += "-Xlint:unchecked",
    libraryDependencies ++= libDependencies
  )
}
