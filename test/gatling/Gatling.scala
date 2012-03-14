package gatling

import java.io.File
import play.api.Play._
import play.Plugin
import play.api.{Application, Play}
import com.excilys.ebi.gatling.core.config.GatlingConfiguration
import scalax.io
import io.Codec
import scalax.file.Path

/**
 *
 * User: noootsab
 * Date: 12/03/12
 * Time: 22:24
 */

class Gatling(app: Application) extends Plugin {


  override def enabled() = isTest(app)

  override def onStart() {
    //todo clean up the file manipulations !!!!


    val gatlingPath: String = "target/gatling/"

    Play.getFile(gatlingPath).mkdirs()

    val gatlingFile = Path(Play.getFile(gatlingPath + "gatling.conf"))
    if (!gatlingFile.exists) {
      gatlingFile.createFile(true, false)
      gatlingFile.write(
        """  | #########################
        | # Gatling Configuration #
        | #########################
        |
        | # This file contains all the settings configurable for Gatling with their default values
        |
        | gatling {
        | 	encoding = "utf-8"              # encoding for every file manipulation made in gatling
        |
        | 	simulation {
        | 		timeout = 86400             # max duration of a simulation in seconds
        | 		scalaPackage = ""
        | 	}
        | 	charting {
        | 		indicators {
        | 			lowerBound  = 100       # in ms
        | 			higherBound = 500       # in ms
        | 		}
        | 	}
        | 	http {
        | 		provider = "Netty"          # Choose between 'Netty', 'JDK', 'Apache' or 'Grizzly'
        | 		compressionEnabled = true   # Set if compression should be supported or not
        | 		connectionTimeout = 60000   # Timeout of the connection to the server (ms)
        | 		requestTimeout = 60000      # Timeout of the requests (ms)
        | 		maxRetry = 5                # number of times that a request should be tried again
        | 	}
        | }
        |
      """.stripMargin)(codec = Codec.UTF8)
    }


    def mkDirsIfNotExists(dir:String):File = Play.getExistingFile(gatlingPath + dir) match {
      case Some(x) => x
      case None => {
        val d = Play.getFile(gatlingPath + dir)
        d.mkdirs()
        d
      }
    }

    val dataFolder: File = mkDirsIfNotExists("data")

    val requestBodiesFolder: File = mkDirsIfNotExists("requestBodies")
    val resultsFolder: File = mkDirsIfNotExists("results")
    val simulationSourcesFolder: File = mkDirsIfNotExists("simulationSources")


    GatlingConfiguration.setUp(
      Some(gatlingFile.path),
      Some(dataFolder.getAbsolutePath),
      Some(requestBodiesFolder.getAbsolutePath),
      Some(resultsFolder.getAbsolutePath),
      Some(simulationSourcesFolder.getAbsolutePath)
    )


  }

}
