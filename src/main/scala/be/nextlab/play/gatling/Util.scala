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
import com.excilys.ebi.gatling.core.scenario.configuration.Simulation

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
      println("Creating run record")
      val runInfo = new RunRecord(now, "run-test", "test" + System.currentTimeMillis)
      println("Run record created > run scenario")
      val runner: Runner = new Runner(runInfo, s())
      runner.run

      println("scenarion ran > generate reports")
      generateReports(runInfo.runUuid)
      println("reports generated")
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
