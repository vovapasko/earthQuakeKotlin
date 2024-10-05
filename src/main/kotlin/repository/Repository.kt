package repository

interface Repository{
    fun fetchEarthQuakeData(): EarthquakeData
}

open class EarthquakeData

class InternalDatabaseRepository: Repository{
    override fun fetchEarthQuakeData(): EarthquakeData {
        TODO("Not yet implemented")
    }

}
