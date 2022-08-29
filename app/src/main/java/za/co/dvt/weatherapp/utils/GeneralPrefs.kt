package za.co.dvt.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import za.co.dvt.weatherapp.database.entity.FavouriteLocation

class GeneralPrefs(context: Context) {
    private var appSharedPref: SharedPreferences? = null
    private var appSharedPrefEditor: SharedPreferences.Editor? = null

    private var activeLocationWeatherPref: SharedPreferences
    private var activeLocationWeatherPrefEditor: SharedPreferences.Editor
    private val LATITUDE = "latitude"
    private val LONGITUDE = "longitude"
    private val PLACE_NAME = "name"


    init {
        appSharedPref =
            context.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE)
        appSharedPrefEditor = appSharedPref?.edit()

        activeLocationWeatherPref =
            context.getSharedPreferences("ACTIVE_LOCATION_PREF", Context.MODE_PRIVATE)
        activeLocationWeatherPrefEditor = activeLocationWeatherPref.edit()
    }

    fun userInitialAppUsage() {
        appSharedPrefEditor?.putBoolean("FIRST_TIME_USAGE", true)
        appSharedPrefEditor?.apply()
        appSharedPrefEditor?.commit()
    }

    fun isFirstTimeUsage(): Boolean? {
        return appSharedPref?.getBoolean("FIRST_TIME_USAGE", false)
    }

    fun storeActiveWeatherLocation(favouriteLocation: FavouriteLocation) {
        activeLocationWeatherPrefEditor.putString(
            LATITUDE,
            favouriteLocation.latitude
        )
        activeLocationWeatherPrefEditor.putString(
            LONGITUDE,
            favouriteLocation.longitude
        )
        activeLocationWeatherPrefEditor.putString(
            PLACE_NAME, favouriteLocation.name
        )
        activeLocationWeatherPrefEditor.apply()
        activeLocationWeatherPrefEditor.commit()
    }

    fun getActiveWeatherLocation(): FavouriteLocation {
        return FavouriteLocation(
            0,
            activeLocationWeatherPref.getString(PLACE_NAME, "").toString(),
            activeLocationWeatherPref.getString(LATITUDE, "").toString(),
            activeLocationWeatherPref.getString(LONGITUDE, "").toString(),
        )
    }

}