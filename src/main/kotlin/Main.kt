package org.example

import org.example.utils.HaversinDistanceCalculator
import models.Coordinate
import models.EarthquakeFromCoordinatePoint
import org.example.engine.EarthquakeEngine
import org.example.extractors.FileEarthquakeDataExtractor
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
        dataExtractor = FileEarthquakeDataExtractor(filePath = "src/main/resources/api-response.json")
    )

    return engine.retrieveClosestEarthquakes(placeCoordinate)
}


fun getCoordinates(): Coordinate {
    val inputList = readln().split(' ').map { it.toDouble() }
    if (inputList.size < 2) {
        throw IllegalArgumentException("must have at least 2 coordinates")
    }
    return Coordinate(inputList[0], inputList[1])
}


