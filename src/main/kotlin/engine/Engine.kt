package org.example.engine

import repository.EarthquakeData
import repository.Repository


abstract class Engine(val repository: Repository) {
    abstract fun getLastEarthquakes(longitude: Double, altitude: Double, earthquakesAmount: Int):
            List<EarthquakeData>
}
