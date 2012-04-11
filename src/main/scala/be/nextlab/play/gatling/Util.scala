package be.nextlab.play.gatling

import com.excilys.ebi.gatling.charts.report.ReportsGenerator
import com.excilys.ebi.gatling.charts.config.ChartsFiles._
import com.excilys.ebi.gatling.core.scenario.configuration.Simulation
import com.excilys.ebi.gatling.core.result.message.RunRecord
import org.joda.time.DateTime._
import com.excilys.ebi.gatling.core.runner.Runner

/**
 *
 * User: noootsab
 * Date: 11/04/12
 * Time: 23:59
 */

object Util {

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

  def gatling(s: Simulation) {
    val runInfo = new RunRecord(now, "run-test", "test")
    new Runner(runInfo, s()).run
    generateReports(runInfo.runUuid)
  }

}
