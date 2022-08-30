package za.co.dvt.weatherapp.database.repository

import androidx.lifecycle.LiveData
import za.co.dvt.weatherapp.database.dao.WeatherPredictionDao
import za.co.dvt.weatherapp.database.entity.WeatherPrediction

class WeatherPredictionRepository(private val weatherPredictionDao: WeatherPredictionDao) {
    val weatherPredictions: LiveData<List<WeatherPrediction>> =
        weatherPredictionDao.getAllWeatherPredictions()

    fun favouriteLocationWeatherPredictions(favouriteLocationId: Int): LiveData<List<WeatherPrediction>> =
        weatherPredictionDao.getWeatherPredictionsByFavouriteLocation(favouriteLocationId)

    suspend fun saveWeatherPrediction(weatherPrediction: WeatherPrediction) {
        weatherPredictionDao.saveWeatherPrediction(weatherPrediction)
    }

    fun getWeatherPredictionsByDate(date: String): WeatherPrediction =
        weatherPredictionDao.getWeatherPredictionsByDate(date)

    fun getCurrentWeatherPredictionsByFavouriteLocation(favouriteLocationId: Int): LiveData<WeatherPrediction> =
        weatherPredictionDao.getCurrentWeatherPredictionsByFavouriteLocation(favouriteLocationId)

    fun getWeatherPredictionById(id: Int): WeatherPrediction =
        weatherPredictionDao.getWeatherPredictionById(id)

    fun deleteWeatherPrediction(favouriteLocationId: Int) {
        weatherPredictionDao.deleteWeatherPrediction(favouriteLocationId)
    }
}