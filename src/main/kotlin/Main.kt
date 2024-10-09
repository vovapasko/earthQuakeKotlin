package org.example

import org.example.data.Coordinate
import org.example.engine.calculateFromCoordinates


fun main() {
    val placeCoordinate = getCoordinates()
    val earthquakes = calculateFromCoordinates(placeCoordinate)
    println(earthquakes)
}


fun getCoordinates(): Coordinate {
    val inputList = readln().split(' ').map { it.toDouble() }
    if (inputList.size < 2) {
        throw IllegalArgumentException("must have at least 2 coordinates")
    }
    return Coordinate(inputList[0], inputList[1])
}


