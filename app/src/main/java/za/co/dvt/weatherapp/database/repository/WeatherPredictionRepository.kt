package za.co.dvt.weatherapp.database.repository

import androidx.lifecycle.LiveData
import za.co.dvt.weatherapp.database.dao.WeatherPredictionDao
import za.co.dvt.weatherapp.database.entity.WeatherPrediction

class WeatherPredictionRepository(private val weatherPredictionDao: WeatherPredictionDao) {
    val weatherPredictions: LiveData<List<WeatherPrediction>> =
        weatherPredictionDao.getWeatherPredictions()

    suspend fun saveWeatherPrediction(weatherPrediction: WeatherPrediction) {
        weatherPredictionDao.saveWeatherPrediction(weatherPrediction)
    }

    fun getWeatherPredictionById(id: Int) {
        weatherPredictionDao.getWeatherPredictionById(id)
    }

    fun deleteWeatherPrediction(weatherPrediction: WeatherPrediction) {
        weatherPredictionDao.deleteWeatherPrediction(weatherPrediction)
    }
}