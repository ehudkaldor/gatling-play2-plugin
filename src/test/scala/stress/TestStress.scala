package stress

import org.specs2.mutable.Specification

import play.api.test._
import play.api.test.Helpers._

import be.nextlab.play.gatling.Util

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

      SampleSimulations.simulations(baseUrl) foreach Util.gatling

    }
  }

}
