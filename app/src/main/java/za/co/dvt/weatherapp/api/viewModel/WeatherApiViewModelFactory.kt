package za.co.dvt.weatherapp.api.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import za.co.dvt.weatherapp.api.repository.WeatherApiRepository

class WeatherApiViewModelFactory constructor(
    private val repository: WeatherApiRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherApiViewModel::class.java)) {
            WeatherApiViewModel(this.repository, this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}