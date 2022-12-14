package za.co.dvt.weatherapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("forecast?")
    suspend fun getWeeklyWeatherInformation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String,
        @Query("appid") openWeatherApiKey: String
    ): Response<WeeklyWeatherDataResponse>
}