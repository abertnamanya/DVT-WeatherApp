package za.co.dvt.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import za.co.dvt.weatherapp.database.dao.FavouriteLocationDao
import za.co.dvt.weatherapp.database.entity.FavouriteLocation

@Database(entities = [FavouriteLocation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavouriteLocationDao(): FavouriteLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "dvt_weather_app_db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}