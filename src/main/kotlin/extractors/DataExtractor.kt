package org.example.extractors

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import models.EarthquakeData
import models.JsonEarthquakeData
import java.io.File

fun getEarthquakeList(dataExtractor: EarthquakeDataExtractor): EarthquakeData {
    return dataExtractor.retrieveEarthquakeData()
}

interface EarthquakeDataExtractor {
    fun retrieveEarthquakeData(): EarthquakeData
}

class FileEarthquakeDataExtractor(private val filePath: String) : EarthquakeDataExtractor {
    private val mapper = jacksonObjectMapper()
    override fun retrieveEarthquakeData(): EarthquakeData {
        println("Retrieving the data from file")
        val jsonFile = File(filePath)
        try {
            val jsonEarthquakeData: JsonEarthquakeData = mapper.readValue(jsonFile)
            return JsonEarthquakeData.to(jsonEarthquakeData)
        } catch (e: MismatchedInputException) {
            throw InternalParsingException(
                "Probably there was a problem with Json parsing of the response. " +
                        "See the response stacktrace ${e.stackTrace}"
            )
        }
    }

    class InternalParsingException(override val message: String?) : Throwable(message)
}

class HTTPEarthquakeDataExtractor(engine: HttpClientEngine = CIO.create()) : EarthquakeDataExtractor {
    private val client = HttpClient(engine)
    private val mapper = jacksonObjectMapper()

    override fun retrieveEarthquakeData() = runBlocking {
        println("Retrieving the data from HTTP")
        val response: HttpResponse =
            client.get("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson")
        val json: String = response.bodyAsText()
        val jsonEarthquakeData: JsonEarthquakeData = mapper.readValue(json)
        return@runBlocking JsonEarthquakeData.to(jsonEarthquakeData)
    }


}
