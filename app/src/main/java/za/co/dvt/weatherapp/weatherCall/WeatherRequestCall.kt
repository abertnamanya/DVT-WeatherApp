package za.co.dvt.weatherapp.weatherCall

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.dvt.weatherapp.data.WeeklyWeatherDataResponse
import za.co.dvt.weatherapp.utils.Constants.Companion.openWeatherApiKey
import za.co.dvt.weatherapp.utils.Constants.Companion.weatherTempUnits
import za.co.dvt.weatherapp.utils.RetrofitService

class WeatherRequestCall {
    var response: Response<WeeklyWeatherDataResponse>? = null

    fun dailyWeatherForeCast(
        latitude: String,
        longitude: String
    ): Response<WeeklyWeatherDataResponse>? {
        val call = RetrofitService.forecastApi.getWeeklyWeatherInformation(
            latitude,
            longitude,
            weatherTempUnits,
            openWeatherApiKey
        )
        call.enqueue(object : Callback<WeeklyWeatherDataResponse> {
            override fun onResponse(
                call: Call<WeeklyWeatherDataResponse>,
                response: Response<WeeklyWeatherDataResponse>
            ) {
                this@WeatherRequestCall.response = response
            }

            override fun onFailure(call: Call<WeeklyWeatherDataResponse>, t: Throwable) {

            }
        })
        return response
    }
}