import io.mockk.mockk
import net.bytebuddy.matcher.ElementMatchers.any
import org.example.data.Coordinate
import org.example.data.EarthquakeData
import org.example.engine.EarthquakeDataExtractor
import org.example.engine.calculateFromCoordinates
import org.example.engine.getEarthquakeList
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull


class MainKtTest {

    @Test
    fun main() {
    }

    @Test
    fun getClosestEarthquakes() {
    }

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