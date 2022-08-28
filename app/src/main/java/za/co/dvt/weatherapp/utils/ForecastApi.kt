package za.co.dvt.weatherapp.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import za.co.dvt.weatherapp.data.WeeklyWeatherDataResponse

interface ForecastApi {

    @GET("forecast?")
    fun getWeeklyWeatherInformation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String,
        @Query("appid") openWeatherApiKey: String
    ): Call<WeeklyWeatherDataResponse>
}