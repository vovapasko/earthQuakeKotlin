package org.example.extractors

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
    override fun retrieveEarthquakeData(): EarthquakeData {
        val mapper = jacksonObjectMapper()
        val jsonFile = File(filePath)
        val jsonEarthquakeData: JsonEarthquakeData = mapper.readValue(jsonFile)
        return JsonEarthquakeData.to(jsonEarthquakeData)
    }
}

// TODO provide HTTP client extractor for https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php