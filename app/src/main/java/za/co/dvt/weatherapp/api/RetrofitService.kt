package za.co.dvt.weatherapp.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private const val openweathermap = "https://api.openweathermap.org/data/2.5/"

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(openweathermap)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val forecastApi: ForecastApi by lazy {
        retrofit.create(ForecastApi::class.java)
    }
}