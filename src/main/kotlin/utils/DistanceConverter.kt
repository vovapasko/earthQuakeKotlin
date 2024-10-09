package org.example.utils


interface DistanceConverter {
    fun convert(distance: Double): Double
}

class MetersToKilometersConverter : DistanceConverter {
    override fun convert(distance: Double): Double {
        return distance / 1000
    }
}
