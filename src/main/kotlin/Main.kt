package org.example

import org.example.client.Client
import org.example.client.ConsoleClient
import org.example.engine.EarthquakeEngine
import org.example.engine.Engine
import repository.InternalDatabaseRepository
import repository.Repository

fun main() {
    val mockClient: Client = ConsoleClient()
    val (longitude, altitude) = mockClient.read()
    val internalRepository: Repository = InternalDatabaseRepository()
    val engine: Engine = EarthquakeEngine(internalRepository)
    val lastEarthquakes = engine.getLastEarthquakes(longitude, altitude, 10)
    println(lastEarthquakes)
}


