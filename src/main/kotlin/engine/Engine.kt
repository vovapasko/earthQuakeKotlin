package org.example.engine

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.lucene.util.SloppyMath.haversinMeters
import org.example.data.Coordinate
import org.example.data.EarthquakeData
import org.example.data.EarthquakeFromCoordinatePoint
import org.example.data.JsonEarthquakeData
import java.io.File


fun calculateFromCoordinates(placeCoordinate: Coordinate): List<EarthquakeFromCoordinatePoint> {
    val earthquakeData = getEarthquakeList(
        FileEarthquakeDataExtractor(filePath = "src/main/resources/api-response.json")
    )
    return getClosestEarthquakes(placeCoordinate, earthquakeData)
}


fun getClosestEarthquakes(
    placeCoordinate: Coordinate,
    earthquakeData: EarthquakeData,
    earthquakesAmount: Int = 10
): List<EarthquakeFromCoordinatePoint> {
    val earthquakeCoordinatesMap: Map<String, Coordinate> = earthquakeData.features.associate {
        val title = it.title
        title to it.geometry.coordinates
    }
    val earthquakeDistanceMap = mutableMapOf<String, Double>()
    earthquakeCoordinatesMap.forEach { earthquakeCoordinate ->
        val (inputLatitude, inputLongitude) = placeCoordinate
        val (earthquakeLatitude, earthquakeLongitude) = earthquakeCoordinate.value
        val meters = haversinMeters(inputLatitude, inputLongitude, earthquakeLatitude, earthquakeLongitude)
        earthquakeDistanceMap[earthquakeCoordinate.key] = meters
    }
    val closestEarthquakes: List<EarthquakeFromCoordinatePoint> =
        earthquakeDistanceMap.entries.sortedBy { it.value }.take(earthquakesAmount).map {
            EarthquakeFromCoordinatePoint(
                title = it.key,
                distance = it.value
            )
        }

    return closestEarthquakes
}

fun getEarthquakeList(dataExtractor: EarthquakeDataExtractor): EarthquakeData {
    return dataExtractor.retrieveEarthquakeData()
}

interface EarthquakeDataExtractor {
    fun retrieveEarthquakeData(): EarthquakeData
}

class FileEarthquakeDataExtractor(private val filePath: String) : EarthquakeDataExtractor {
    override fun retrieveEarthquakeData(): EarthquakeData {
        val mapper = jacksonObjectMapper()
        val jsonFile = File(filePath)
        val jsonEarthquakeData: JsonEarthquakeData = mapper.readValue(jsonFile)
        return JsonEarthquakeData.to(jsonEarthquakeData)
    }

}