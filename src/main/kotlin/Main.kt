package org.example

import org.example.utils.HaversinDistanceCalculator
import models.Coordinate
import models.EarthquakeFromCoordinatePoint
import org.example.engine.EarthquakeEngine
import org.example.extractors.HTTPEarthquakeDataExtractor
import org.example.utils.MetersToKilometersConverter


fun main() {
    val placeCoordinate = getCoordinates()
    val earthquakes = calculateFromCoordinates(placeCoordinate)
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


