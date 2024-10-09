import io.mockk.mockk
import models.Coordinate
import models.EarthquakeData
import org.example.calculateFromCoordinates
import org.example.extractors.EarthquakeDataExtractor
import org.example.extractors.getEarthquakeList
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull


class MainKtTest {


    @Test
    fun `should return earthquake list`() {
        val earthquakeFeature = getEarthquakeList(MockDataExtractor())
        assertNotNull(earthquakeFeature)
    }

    @Test
    fun `test main program with given coordinates`() {
        val earthquakes = calculateFromCoordinates(
            Coordinate(
                35.689487,
                139.691711
            )
        )
        assertNotNull(earthquakes)
        println(earthquakes)
    }
}

class MockDataExtractor : EarthquakeDataExtractor {
    override fun retrieveEarthquakeData(): EarthquakeData {
        return EarthquakeData(mockk(), mockk())
    }
}