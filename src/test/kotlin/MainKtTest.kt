import io.mockk.mockk
import models.Coordinate
import models.EarthquakeData
import org.example.calculateFromCoordinates
import org.example.engine.EarthquakeEngine
import org.example.extractors.EarthquakeDataExtractor
import org.example.extractors.FileEarthquakeDataExtractor
import org.example.extractors.getEarthquakeList
import org.example.utils.HaversinDistanceCalculator
import org.example.utils.MetersToKilometersConverter
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
        val locationCoordinates = Coordinate(
            35.689487,
            139.691711
        )
        val engine = EarthquakeEngine(
            HaversinDistanceCalculator(),
            MetersToKilometersConverter(),
            FileEarthquakeDataExtractor(filePath = "src/main/resources/api-response.json"),
        )
        val earthquakes = engine.retrieveClosestEarthquakes(locationCoordinates)
        assertNotNull(earthquakes)
        println(earthquakes)
    }
}

class MockDataExtractor : EarthquakeDataExtractor {
    override fun retrieveEarthquakeData(): EarthquakeData {
        return EarthquakeData(mockk(), mockk())
    }
}