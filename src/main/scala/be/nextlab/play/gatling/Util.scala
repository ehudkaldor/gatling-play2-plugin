package be.nextlab.play.gatling

import com.excilys.ebi.gatling.charts.report.ReportsGenerator
import com.excilys.ebi.gatling.charts.config.ChartsFiles._
import com.excilys.ebi.gatling.core.result.message.RunRecord
import org.joda.time.DateTime._
import com.excilys.ebi.gatling.core.runner.Runner
import org.specs2.specification.Around
import org.specs2.execute.Result
import play.api.test.Helpers._
import play.api.test.{TestServer, FakeApplication}
import com.excilys.ebi.gatling.core.result.writer.DataWriter
import akka.util.Duration._
import com.excilys.ebi.gatling.core.config.GatlingConfiguration._
import java.util.concurrent.TimeUnit._
import akka.util.Duration
import akka.util.duration.longToDurationLong
import com.excilys.ebi.gatling.core.action.system
import com.excilys.ebi.gatling.core.scenario.configuration.{ScenarioConfiguration, ScenarioConfigurationBuilder, Simulation}
import akka.actor.ActorRef
import com.excilys.ebi.gatling.core.session.Session
import com.excilys.ebi.gatling.app.Gatling._
import com.excilys.ebi.gatling.core.resource.ResourceRegistry

/**
 *
 * User: noootsab
 * Date: 11/04/12
 * Time: 23:59
 */

object Util {

  lazy val fakeApplication: FakeApplication = FakeApplication(additionalPlugins = Seq("be.nextlab.play.gatling.GatlingPlugin"))
  lazy val plugin: GatlingPlugin = fakeApplication.plugin[GatlingPlugin].get

  def createServer(port:Int, application:FakeApplication = fakeApplication) = new TestServer(port, application)

  case class GatlingApp(s:Simulation) extends Around {

    def around[T <% Result](t: => T) = {
      gatling(s)
      t
    }

    def gatling(s: Simulation) {
      //useActorSystem {
        println("Creating run record")
        val runInfo = new RunRecord(now, "run-test", "test" + System.currentTimeMillis)
        println("Run record created > run scenario")

        val configurations = s()
        new Runner(runInfo, configurations).run

        println("Simulation Finished.")
        runInfo.runUuid

        println("scenarion ran > generate reports")
        generateReports(runInfo.runUuid)
        println("reports generated")

        //ResourceRegistry.closeAll
      //}
    }

    def generateReports(runUuid: String) {
      println("Generating reports...")
      val start = System.currentTimeMillis
      if (ReportsGenerator.generateFor(runUuid)) {
        println("Reports generated in " + (System.currentTimeMillis - start) / 1000 + "s.")
        println("Please open the following file : " + activeSessionsFile(runUuid))
      } else {
        println("Reports weren't generated")
      }
    }
  }


}
