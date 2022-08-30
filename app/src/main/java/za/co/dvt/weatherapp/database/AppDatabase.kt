package za.co.dvt.weatherapp.database

import android.content.Context
import androidx.room.*
import za.co.dvt.weatherapp.database.dao.FavouriteLocationDao
import za.co.dvt.weatherapp.database.dao.WeatherPredictionDao
import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.database.entity.WeatherPrediction

@Database(
    entities = [FavouriteLocation::class, WeatherPrediction::class],
    version = 1,
    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavouriteLocationDao(): FavouriteLocationDao
    abstract fun WeatherPredictionDao(): WeatherPredictionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "dvt_weather_app_db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}