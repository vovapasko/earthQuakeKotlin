package org.example.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlin.random.Random


@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonEarthquakeData(
    val metadata: Map<String, String>,
    val features: List<JsonEarthquakeFeature>,
) {
    companion object {
        fun to(jsonData: JsonEarthquakeData) = EarthquakeData(
            metadata = jsonData.metadata,
            features = jsonData.features.map { JsonEarthquakeFeature.to(it) }
        )
    }
}

data class EarthquakeData(
    val metadata: Map<String, String>,
    val features: List<EarthquakeFeature>,
)

data class JsonEarthquakeGeometry(
    val type: String,
    val coordinates: List<Double>
) {
    companion object {
        fun to(jsonGeometry: JsonEarthquakeGeometry) = EarthquakeGeometry(
            type = jsonGeometry.type,
            coordinates = Coordinate(
                jsonGeometry.coordinates[0],
                jsonGeometry.coordinates[1],
                jsonGeometry.coordinates[2]
            )
        )
    }
}

data class EarthquakeGeometry(
    val type: String,
    val coordinates: Coordinate
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonEarthquakeFeature(val properties: Map<String, String>, val geometry: JsonEarthquakeGeometry) {
    companion object {
        fun to(jsonFeature: JsonEarthquakeFeature) = EarthquakeFeature(
            properties = jsonFeature.properties,
            title = jsonFeature.properties["title"] ?: "Untitled earthquake ${Random.nextInt(0, Int.MAX_VALUE)}",
            geometry = JsonEarthquakeGeometry.to(jsonFeature.geometry)
        )
    }
}

data class EarthquakeFeature(val properties: Map<String, String>, val title: String, val geometry: EarthquakeGeometry)


data class Coordinate(val latitude: Double, val longitude: Double, val depth: Double = 0.0)

data class EarthquakeFromCoordinatePoint(val title: String, val distance: Double) {
    override fun toString(): String {
        return "$title || ${distance.toInt()}"
    }
}