package stress

import org.specs2.mutable.Specification

import play.api._
import play.api.Play._
import play.api.test._
import play.api.test.Helpers._

import org.joda.time.DateTime
import java.io.File
import Play._
import com.excilys.ebi.gatling.core.result.message.RunRecord
import org.joda.time.DateTime._
import com.excilys.ebi.gatling.core.runner.Runner
import com.excilys.ebi.gatling.core.config.GatlingConfiguration
import com.excilys.ebi.gatling.charts.report.ReportsGenerator
import com.excilys.ebi.gatling.charts.config.ChartsFiles._
import com.excilys.ebi.gatling.core.scenario.configuration.Simulation

/**
 *
 * User: noootsab
 * Date: 12/03/12
 * Time: 22:28
 */

class TestStress extends Specification {

  "stress test the server" in {
    val baseUrl = "http://localhost:3333"
    running(TestServer(3333, FakeApplication(
    additionalPlugins = Seq("gatling.Gatling")
    ))) {

      SampleSimulations.simulations(baseUrl) foreach gatling

    }
  }


  private def generateReports(runUuid: String) {
    println("Generating reports...")
    val start = System.currentTimeMillis
    if (ReportsGenerator.generateFor(runUuid)) {
      println("Reports generated in " + (System.currentTimeMillis - start) / 1000 + "s.")
      println("Please open the following file : " + activeSessionsFile(runUuid))
    } else {
      println("Reports weren't generated")
    }
  }

  def gatling(s:Simulation) {
    val runInfo = new RunRecord(now, "run-test", "test")
    new Runner(runInfo, s()).run
    generateReports(runInfo.runUuid)
  }

}
