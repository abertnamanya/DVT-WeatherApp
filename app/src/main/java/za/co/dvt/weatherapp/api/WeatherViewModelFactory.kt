package za.co.dvt.weatherapp.api

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory constructor(
    private val repository: WeatherRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(this.repository, this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}