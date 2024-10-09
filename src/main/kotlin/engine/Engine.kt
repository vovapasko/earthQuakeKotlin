package org.example.engine

import org.example.calculator.DistanceCoordinateCalculator
import org.example.calculator.HaversinDistanceCalculator
import org.example.data.Coordinate
import org.example.data.EarthquakeData
import org.example.data.EarthquakeFromCoordinatePoint
import org.example.extractors.FileEarthquakeDataExtractor
import org.example.extractors.getEarthquakeList


fun calculateFromCoordinates(placeCoordinate: Coordinate): List<EarthquakeFromCoordinatePoint> {
    val earthquakeData = getEarthquakeList(
        FileEarthquakeDataExtractor(filePath = "src/main/resources/api-response.json")
    )
    return getClosestEarthquakes(placeCoordinate, earthquakeData, HaversinDistanceCalculator())
}


fun getClosestEarthquakes(
    placeCoordinate: Coordinate,
    earthquakeData: EarthquakeData,
    coordinateCalculator: DistanceCoordinateCalculator,
    earthquakesAmount: Int = 10
): List<EarthquakeFromCoordinatePoint> {
    val earthquakeCoordinatesMap: Map<String, Coordinate> = earthquakeData.features.associate {
        val title = it.title
        title to it.geometry.coordinates
    }
    val earthquakeDistanceMap = mutableMapOf<String, Double>()
    earthquakeCoordinatesMap.forEach { earthquakeCoordinate ->
        val meters = calculateDistanceBetweenPoints(placeCoordinate, earthquakeCoordinate.value, coordinateCalculator)
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

private fun calculateDistanceBetweenPoints(
    inputCoordinate: Coordinate,
    earthquakeCoordinate: Coordinate,
    distanceCoordinateCalculator: DistanceCoordinateCalculator
): Double {
    return distanceCoordinateCalculator.calculateDistanceInMeters(inputCoordinate, earthquakeCoordinate)
}

