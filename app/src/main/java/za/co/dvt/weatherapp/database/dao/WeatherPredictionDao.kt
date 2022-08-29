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
    fun getAllWeatherPredictions(): LiveData<List<WeatherPrediction>>

    @Query("SELECT * FROM WeatherPrediction WHERE favourite_location_id=:favouriteLocationId ORDER BY id DESC")
    fun getWeatherPredictionsByFavouriteLocation(favouriteLocationId: Int): LiveData<List<WeatherPrediction>>

    @Query("SELECT * FROM WeatherPrediction where id=:id")
    fun getWeatherPredictionById(id: Int): WeatherPrediction

    @Query("SELECT * FROM WeatherPrediction where id=:id")
    fun getWeatherPredictionByLocation(id: Int): WeatherPrediction

    @Query("SELECT * FROM WeatherPrediction where weather_date=:date")
    fun getWeatherPredictionsByDate(date: String): WeatherPrediction

    @Query("DELETE FROM WeatherPrediction WHERE favourite_location_id =:favouriteLocationId")
    fun deleteWeatherPrediction(favouriteLocationId: Int)
}