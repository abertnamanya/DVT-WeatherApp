package za.co.dvt.weatherapp.api.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import za.co.dvt.weatherapp.api.ForecastApi

class WeatherApiRepository constructor(private val api: ForecastApi) {
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