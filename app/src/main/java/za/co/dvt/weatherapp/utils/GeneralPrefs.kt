package za.co.dvt.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences

class GeneralPrefs(context: Context) {
    private var appSharedPref: SharedPreferences? = null
    private var appSharedPrefEditor: SharedPreferences.Editor? = null

    init {
        appSharedPref =
            context.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE)
        appSharedPrefEditor = appSharedPref?.edit()
    }

    fun userInitialAppUsage() {
        appSharedPrefEditor?.putBoolean("FIRST_TIME_USAGE", true)
        appSharedPrefEditor?.apply()
        appSharedPrefEditor?.commit()
    }

    fun isFirstTimeUsage(): Boolean? {
        return appSharedPref?.getBoolean("FIRST_TIME_USAGE", false)
    }

}