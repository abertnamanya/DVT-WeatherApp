package za.co.dvt.weatherapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository constructor(private val api: ForecastApi) {
    suspend fun getWeeklyWeatherInformation(
        latitude: String,
        longitude: String,
        units: String,
        openWeatherApiKey: String
    ) = withContext(
        Dispatchers.IO
    ) {
        var call = api.getWeeklyWeatherInformation(latitude, longitude, units, openWeatherApiKey)
        call
    }
}