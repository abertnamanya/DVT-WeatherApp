package za.co.dvt.weatherapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WeatherPrediction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "favourite_location_id") val favourite_location_id: Int,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "temp_min") val temp_min: Double,
    @ColumnInfo(name = "temp_max") val temp_max: Double,
    @ColumnInfo(name = "weather") val weather: String,
    @ColumnInfo(name = "weather_description") val weather_description: String,
    @ColumnInfo(name = "weather_icon") val weather_icon: String,
    @ColumnInfo(name = "weather_date") val weather_date: String
)
