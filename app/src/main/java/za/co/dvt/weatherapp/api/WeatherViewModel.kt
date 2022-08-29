package za.co.dvt.weatherapp.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import za.co.dvt.weatherapp.data.WeeklyWeatherDataResponse
import za.co.dvt.weatherapp.utils.checkForInternet
import java.io.IOException

class WeatherViewModel constructor(
    private val repository: WeatherRepository,
    private val context: Context
) : ViewModel() {

    val weatherInfo: MutableLiveData<Resource<WeeklyWeatherDataResponse>> =
        MutableLiveData()

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
                        weatherInfo.postValue(Resource.Success(response.body()!!))
                    } else {
                        weatherInfo.postValue(Resource.Error(" ${response.message()}"))
                    }
                } else {
                    weatherInfo.postValue(Resource.Error("No Internet Connection"))
                }
            } catch (ex: Exception) {
                when (ex) {
                    //make offline query of the data
                    is IOException -> weatherInfo.postValue(Resource.Error("Network Connection " + ex.localizedMessage))
                    else -> weatherInfo.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }
}