package org.example.engine

import repository.EarthquakeData
import repository.Repository

class EarthquakeEngine(repository: Repository): Engine(repository) {
    override fun getLastEarthquakes(longitude: Double, altitude: Double, earthquakesAmount: Int): List<EarthquakeData> {
        return repository.fetchEarthQuakeData()
    }
}