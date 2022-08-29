package za.co.dvt.weatherapp.api.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import za.co.dvt.weatherapp.api.Resource
import za.co.dvt.weatherapp.api.WeeklyWeatherDataResponse
import za.co.dvt.weatherapp.database.AppDatabase
import za.co.dvt.weatherapp.database.entity.WeatherPrediction
import za.co.dvt.weatherapp.database.repository.WeatherPredictionRepository
import za.co.dvt.weatherapp.api.repository.WeatherApiRepository
import za.co.dvt.weatherapp.utils.GeneralPrefs
import za.co.dvt.weatherapp.utils.checkForInternet
import java.io.IOException

class WeatherApiViewModel constructor(
    private val repository: WeatherApiRepository,
    private val context: Context
) : ViewModel() {

    val weatherInfo: MutableLiveData<Resource<WeeklyWeatherDataResponse>> =
        MutableLiveData()

    private val weatherPredictionRepository: WeatherPredictionRepository
    private var selectedLocationPref: GeneralPrefs

    init {
        val weatherPredictionDao = AppDatabase.getInstance(context)?.WeatherPredictionDao()
        weatherPredictionRepository = WeatherPredictionRepository(weatherPredictionDao!!)
        selectedLocationPref = GeneralPrefs(context)
    }

    fun getWeeklyWeatherInformation(
        latitude: String,
        longitude: String,
        units: String,
        openWeatherApiKey: String
    ) {
        weatherInfo.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                if (checkForInternet(context)) {
                    val response: Response<WeeklyWeatherDataResponse> =
                        repository.getWeeklyWeatherInformation(
                            latitude,
                            longitude,
                            units,
                            openWeatherApiKey
                        )
                    if (response.isSuccessful) {
                        var favouriteLocationId = selectedLocationPref.getActiveWeatherLocation().uuid
                        //delete all previous predictions
                        weatherPredictionRepository.deleteWeatherPrediction(favouriteLocationId)
                        try {
                            for (weatherConditions in response.body()!!.list) {
                                weatherPredictionRepository.saveWeatherPrediction(
                                    WeatherPrediction(
                                        0,
                                        favouriteLocationId,
                                        weatherConditions.main.temp,
                                        weatherConditions.main.temp_min,
                                        weatherConditions.main.temp_max,
                                        weatherConditions.weather[0].main,
                                        weatherConditions.weather[0].description,
                                        weatherConditions.weather[0].icon,
                                        weatherConditions.dt_txt
                                    )
                                )
                            }
                        } catch (ex: Exception) {
                            ex.stackTrace
                        }
                        weatherInfo.postValue(Resource.Success(response.body()!!))
                    } else {
                        weatherInfo.postValue(Resource.Error(" ${response.message()}"))
                    }
                } else {
                    weatherInfo.postValue(Resource.Error("No Internet Connection"))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> weatherInfo.postValue(Resource.Error("Network Connection " + ex.localizedMessage))
                    else -> weatherInfo.postValue(Resource.Error("Conversion Error ${ex.message}"))
                }
            }
        }
    }
}