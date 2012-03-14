import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "gatling-play2-plugin"
  val appVersion = "1.0"

  /* OFFICIAL GATLING REPO */
  val gatlingReleasesRepo = "Gatling Releases Repo" at "http://repository.excilys.com/content/repositories/releases"
  val gatling3PartyRepo = "Gatling Third-Party Repo" at "http://repository.excilys.com/content/repositories/thirdparty"


  /* LOCAL MAVEN REPO for My Forked Gatling */
  val mavenLocal = "Local Maven Repository" at "file:///"+ Path.userHome+"/.m2/repository"


  /* GATLING DEPS */
  val gatlingVersionNumber = "1.1"                    //FUTURE version that should have Akka 2.0 support !
  val gatlingSnapshotVersionNumber = "1.1.0-SNAPSHOT" //My version of the 1.1.0-SNAPSHOT that has rough Akka 2.0 support
  val gatlingApp = "com.excilys.ebi.gatling" % "gatling-app" % gatlingSnapshotVersionNumber
  val gatlingRecorder = "com.excilys.ebi.gatling" % "gatling-recorder" % gatlingSnapshotVersionNumber
  val gatlingCharts = "com.excilys.ebi.gatling" % "gatling-charts" % gatlingSnapshotVersionNumber
  val gatlingHighcharts = "com.excilys.ebi.gatling.highcharts" % "gatling-charts-highcharts" % gatlingSnapshotVersionNumber


  val appDependencies = Seq(
    gatlingApp,
    gatlingRecorder,
    gatlingCharts,
    gatlingHighcharts
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    resolvers ++= Seq(
      gatlingReleasesRepo,
      gatling3PartyRepo,

      mavenLocal
    )
  )

}
