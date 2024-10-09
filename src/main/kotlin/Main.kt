package org.example

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class Coordinate(val latitude: Double, val longitude: Double, val depth: Double = 0.0) {
    companion object {
        fun from(doubles: List<Double>) = Coordinate(latitude = doubles[0], longitude = doubles[1], depth = doubles[2])
    }
}

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

fun getEarthquakeList(): List<EarthquakeFeature> {
    val filePath = "src/main/resources/api-response.json"
    val mapper = jacksonObjectMapper()
    val jsonFile = File(filePath)
    val earthquakeFeature: EarthquakeData = mapper.readValue(jsonFile)
    return earthquakeFeature.features
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class EarthquakeData(
    val metadata: Map<String, String>,
    val features: List<EarthquakeFeature>,
)

data class EarthquakeGeometry(
    val type: String,
    val coordinates: List<Double>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EarthquakeFeature(val properties: Map<String, String>, val geometry: EarthquakeGeometry)

fun getCoordinates(): Coordinate {
    val inputList = readln().split(' ').map { it.toDouble() }
    if (inputList.size < 2) {
        throw IllegalArgumentException("must have at least 2 coordinates")
    }
    return Coordinate(inputList[0], inputList[1])
}


