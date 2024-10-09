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
    fun getCoordinates() {
    }
}