package za.co.dvt.weatherapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import za.co.dvt.weatherapp.database.AppDatabase
import za.co.dvt.weatherapp.database.entity.WeatherPrediction
import za.co.dvt.weatherapp.database.repository.WeatherPredictionRepository
import za.co.dvt.weatherapp.utils.GeneralPrefs

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    val locationWeatherPredictions: LiveData<List<WeatherPrediction>>
    private val repository: WeatherPredictionRepository
    private var selectedLocationPref: GeneralPrefs

    init {
        val weatherPredictionDao = AppDatabase.getInstance(application)?.WeatherPredictionDao()
        repository = WeatherPredictionRepository(weatherPredictionDao!!)
        selectedLocationPref = GeneralPrefs(application)
        locationWeatherPredictions =
            repository.favouriteLocationWeatherPredictions(selectedLocationPref.getActiveWeatherLocation().uuid)
    }
}