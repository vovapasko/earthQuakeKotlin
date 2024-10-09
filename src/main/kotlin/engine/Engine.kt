package org.example.engine

import org.example.utils.DistanceCoordinateCalculator
import models.Coordinate
import models.EarthquakeData
import models.EarthquakeFromCoordinatePoint
import org.example.extractors.EarthquakeDataExtractor
import org.example.utils.DistanceConverter


class EarthquakeEngine(
    private val coordinateCalculator: DistanceCoordinateCalculator,
    private val measureConverter: DistanceConverter,
    private val dataExtractor: EarthquakeDataExtractor,
) {

    fun retrieveClosestEarthquakes(coordinate: Coordinate, earthquakesAmount: Int = 10): List<EarthquakeFromCoordinatePoint> {
        val earthquakeData = dataExtractor.retrieveEarthquakeData()
        return getClosestEarthquakes(coordinate, earthquakeData, earthquakesAmount)
    }

    private fun getClosestEarthquakes(
        placeCoordinate: Coordinate,
        earthquakeData: EarthquakeData,
        earthquakesAmount: Int,
    ): List<EarthquakeFromCoordinatePoint> {
        val earthquakeCoordinatesMap: Map<String, Coordinate> = earthquakeData.features.associate {
            val title = it.title
            title to it.geometry.coordinates
        }
        val earthquakeDistanceMap = mutableMapOf<String, Double>()
        earthquakeCoordinatesMap.forEach { earthquakeCoordinate ->
            val meters =
                calculateDistanceBetweenPoints(placeCoordinate, earthquakeCoordinate.value, coordinateCalculator)
            earthquakeDistanceMap[earthquakeCoordinate.key] = meters
        }
        val closestEarthquakes: List<EarthquakeFromCoordinatePoint> =
            earthquakeDistanceMap.entries.sortedBy { it.value }.take(earthquakesAmount).map {
                EarthquakeFromCoordinatePoint(
                    title = it.key,
                    distance = measureConverter.convert(it.value)
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

}
