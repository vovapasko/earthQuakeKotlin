import io.ktor.client.engine.mock.*
import io.ktor.http.*
import models.EarthquakeData
import org.example.extractors.FileEarthquakeDataExtractor
import org.example.extractors.FileEarthquakeDataExtractor.InternalParsingException
import org.example.extractors.HTTPEarthquakeDataExtractor
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest

class DataExtractorKtTest {

    @Nested
    inner class FileEarthquakeDataExtractorTest {

        @Test
        fun `should return list of EarthquakeData`() {
            val dataExtractor =
                FileEarthquakeDataExtractor(filePath = "src/test/kotlin/resources/correct-api-response.json")
            val data: EarthquakeData = dataExtractor.retrieveEarthquakeData()
            assertNotNull(data.metadata)
            assertNotNull(data.features)
        }

        @Test
        fun `should raise not found exception if there is no such a file`() {
            val filePath = "src/test/kotlin/resources/non-existing-response.json"
            val dataExtractor =
                FileEarthquakeDataExtractor(filePath = filePath)
            val exception = assertThrows<java.io.FileNotFoundException> {
                dataExtractor.retrieveEarthquakeData()
            }
            assertEquals("$filePath (No such file or directory)", exception.message)
        }

        @Test
        fun `should raise parsing error if there is no metadata or features in the response`() {
            val dataExtractor =
                FileEarthquakeDataExtractor(filePath = "src/test/kotlin/resources/incorrect-api-response.json")
            assertThrows<InternalParsingException> {
                dataExtractor.retrieveEarthquakeData()
            }
        }
    }

    @Nested
    inner class HTTPEarthquakeDataExtractorTest {

        private val validJson = """
            {
              "metadata": {
                "generated": 1728158626000,
                "url": "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson",
                "title": "USGS All Earthquakes, Past Month",
                "status": 200,
                "api": "1.10.3",
                "count": 9363
              },
              "features": [
                {
                  "type": "Feature",
                  "properties": {
                    "mag": 0.6,
                    "place": "18 km ESE of Julian, CA",
                    "time": 1728158178720,
                    "updated": 1728158556363,
                    "tz": null,
                    "url": "https://earthquake.usgs.gov/earthquakes/eventpage/ci40752503",
                    "detail": "https://earthquake.usgs.gov/earthquakes/feed/v1.0/detail/ci40752503.geojson",
                    "felt": null,
                    "cdi": null,
                    "mmi": null,
                    "alert": null,
                    "status": "automatic",
                    "tsunami": 0,
                    "sig": 6,
                    "net": "ci",
                    "code": "40752503",
                    "ids": ",ci40752503,",
                    "sources": ",ci,",
                    "types": ",nearby-cities,origin,phase-data,scitech-link,",
                    "nst": 31,
                    "dmin": 0.04264,
                    "rms": 0.14,
                    "gap": 42,
                    "magType": "ml",
                    "type": "earthquake",
                    "title": "M 0.6 - 18 km ESE of Julian, CA"
                  },
                  "geometry": {
                    "type": "Point",
                    "coordinates": [
                      -116.4186667,
                      33.0378333,
                      8.91
                    ]
                  },
                  "id": "ci40752503"
                }
              ]
            }
        """.trimIndent()

        @Test
        fun `test retrieveEarthquakeData`() = runTest {
            val mockEngine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(validJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            val dataExtractor = HTTPEarthquakeDataExtractor(mockEngine)
            val data: EarthquakeData = dataExtractor.retrieveEarthquakeData()
            assertNotNull(data.metadata)
            assertNotNull(data.features)
        }
    }
}

