package org.example

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.lucene.util.SloppyMath.haversinMeters
import java.io.File
import kotlin.random.Random

data class Coordinate(val latitude: Double, val longitude: Double, val depth: Double = 0.0)

data class EarthquakeFromCoordinatePoint(val title: String, val distance: Double) {
    override fun toString(): String {
        return "$title || ${distance.toInt()}"
    }
}

fun main() {
    val placeCoordinate = getCoordinates()
    val earthquakes = calculateFromCoordinates(placeCoordinate)
    println(earthquakes)
}

fun calculateFromCoordinates(placeCoordinate: Coordinate): List<EarthquakeFromCoordinatePoint> {
    val earthquakeData = getEarthquakeList()
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

fun getEarthquakeList(): EarthquakeData {
    val filePath = "src/main/resources/api-response.json"
    val mapper = jacksonObjectMapper()
    val jsonFile = File(filePath)
    val jsonEarthquakeData: JsonEarthquakeData = mapper.readValue(jsonFile)
    return JsonEarthquakeData.to(jsonEarthquakeData)
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonEarthquakeData(
    val metadata: Map<String, String>,
    val features: List<JsonEarthquakeFeature>,
) {
    companion object {
        fun to(jsonData: JsonEarthquakeData) = EarthquakeData(
            metadata = jsonData.metadata,
            features = jsonData.features.map { JsonEarthquakeFeature.to(it) }
        )
    }
}

data class EarthquakeData(
    val metadata: Map<String, String>,
    val features: List<EarthquakeFeature>,
)

data class JsonEarthquakeGeometry(
    val type: String,
    val coordinates: List<Double>
) {
    companion object {
        fun to(jsonGeometry: JsonEarthquakeGeometry) = EarthquakeGeometry(
            type = jsonGeometry.type,
            coordinates = Coordinate(
                jsonGeometry.coordinates[0],
                jsonGeometry.coordinates[1],
                jsonGeometry.coordinates[2]
            )
        )
    }
}

data class EarthquakeGeometry(
    val type: String,
    val coordinates: Coordinate
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonEarthquakeFeature(val properties: Map<String, String>, val geometry: JsonEarthquakeGeometry) {
    companion object {
        fun to(jsonFeature: JsonEarthquakeFeature) = EarthquakeFeature(
            properties = jsonFeature.properties,
            title = jsonFeature.properties["title"] ?: "Untitled earthquake ${Random.nextInt(0, Int.MAX_VALUE)}",
            geometry = JsonEarthquakeGeometry.to(jsonFeature.geometry)
        )
    }
}

data class EarthquakeFeature(val properties: Map<String, String>, val title: String, val geometry: EarthquakeGeometry)

fun getCoordinates(): Coordinate {
    val inputList = readln().split(' ').map { it.toDouble() }
    if (inputList.size < 2) {
        throw IllegalArgumentException("must have at least 2 coordinates")
    }
    return Coordinate(inputList[0], inputList[1])
}


