package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.example.utils.HaversinDistanceCalculator
import models.Coordinate
import models.EarthquakeFromCoordinatePoint
import org.example.engine.EarthquakeEngine
import org.example.extractors.HTTPEarthquakeDataExtractor
import org.example.utils.MetersToKilometersConverter


fun main() {
//    val placeCoordinate = getCoordinates()
    val testCoordinates = Coordinate(
        35.652832, 139.839478
    )
    val earthquakes = calculateFromCoordinates(testCoordinates)
    println(earthquakes)
}


fun calculateFromCoordinates(placeCoordinate: Coordinate): List<EarthquakeFromCoordinatePoint> {
    val engine = EarthquakeEngine(
        coordinateCalculator = HaversinDistanceCalculator(),
        measureConverter = MetersToKilometersConverter(),
        dataExtractor = HTTPEarthquakeDataExtractor()
    )

    return engine.retrieveClosestEarthquakes(placeCoordinate)
}


fun getCoordinates(): Coordinate {
    val latitude = readln().toDouble()
    val longitude = readln().toDouble()
    return Coordinate(latitude, longitude)
}


