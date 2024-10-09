import org.example.Coordinate
import org.example.calculateFromCoordinates
import org.example.getEarthquakeList
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
        val earthquakeFeature = getEarthquakeList()
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