import com.fasterxml.jackson.core.JsonParseException
import models.EarthquakeData
import org.example.extractors.FileEarthquakeDataExtractor
import org.example.extractors.FileEarthquakeDataExtractor.InternalParsingException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


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
}

