package org.example

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class Coordinate(val latitude: Double, val longitude: Double, val depth: Double = 0.0)

data class EarthquakeFromCoordinatePoint(val title: String, val distance: Double) {
    override fun toString(): String {
        return "$title || $distance"
    }
}

fun main() {
    val placeCoordinate = getCoordinates()
    println(placeCoordinate)
    val earthquakeList = getEarthquakeList()
    val closestEarthquakesToThePoint = getClosestEarthquakes(placeCoordinate, earthquakeList)
    closestEarthquakesToThePoint.forEach { println(it) }
}

fun getClosestEarthquakes(
    placeCoordinate: Coordinate,
    earthquakeList: Any,
    earthquakesAmount: Int = 10
): List<EarthquakeFromCoordinatePoint> {
    return emptyList()
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
            geometry = JsonEarthquakeGeometry.to(jsonFeature.geometry)
        )
    }
}

data class EarthquakeFeature(val properties: Map<String, String>, val geometry: EarthquakeGeometry)

fun getCoordinates(): Coordinate {
    val inputList = readln().split(' ').map { it.toDouble() }
    if (inputList.size < 2) {
        throw IllegalArgumentException("must have at least 2 coordinates")
    }
    return Coordinate(inputList[0], inputList[1])
}


