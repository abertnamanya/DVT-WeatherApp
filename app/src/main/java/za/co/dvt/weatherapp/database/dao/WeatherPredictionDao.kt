package za.co.dvt.weatherapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import za.co.dvt.weatherapp.database.entity.LocationPredictions
import za.co.dvt.weatherapp.database.entity.WeatherPrediction

@Dao
interface WeatherPredictionDao {

    @Transaction
    @Query("SELECT * FROM FavouriteLocation")
    fun getLocationWeatherPredictions(): List<LocationPredictions>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveWeatherPrediction(weatherPrediction: WeatherPrediction)

    @Query("SELECT * FROM WeatherPrediction ORDER BY id DESC")
    fun getWeatherPredictions(): LiveData<List<WeatherPrediction>>

    @Query("SELECT * FROM WeatherPrediction where id=:id")
    fun getWeatherPredictionById(id: Int)

    @Delete
    fun deleteWeatherPrediction(weatherPrediction: WeatherPrediction)
}