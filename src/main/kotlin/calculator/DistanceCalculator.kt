package org.example.calculator

import org.apache.lucene.util.SloppyMath.haversinMeters
import org.example.data.Coordinate


interface DistanceCoordinateCalculator {
    fun calculateDistanceInMeters(coordinateA: Coordinate, coordinateB: Coordinate): Double
}

class HaversinDistanceCalculator : DistanceCoordinateCalculator {
    override fun calculateDistanceInMeters(coordinateA: Coordinate, coordinateB: Coordinate): Double {
        val (latitudeA, longitudeA) = coordinateA
        val (latitudeB, longitudeB) = coordinateB
        return haversinMeters(latitudeA, longitudeA, latitudeB, longitudeB)
    }
}