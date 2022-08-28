package za.co.dvt.locationtracker

import android.content.Context
import android.content.SharedPreferences

class LocationSharedPref(context: Context) {
    private var locationSharedPref: SharedPreferences? = null
    private var locationSharedPrefEditor: SharedPreferences.Editor? = null
    private var latitude: String = "latitude"
    private var longitude: String = "longitude"

    init {
        locationSharedPref =
            context.getSharedPreferences("CURRENT_LOCATION_PREF", Context.MODE_PRIVATE)
        locationSharedPrefEditor = locationSharedPref?.edit()
    }

    fun storeCurrentLocation(locationData: LocationData) {
        locationSharedPrefEditor?.putString(latitude, locationData.latitude)
        locationSharedPrefEditor?.putString(longitude, locationData.longitude)
        locationSharedPrefEditor?.apply()
        locationSharedPrefEditor?.commit()
    }

    fun removePreviousLocationData() {
        locationSharedPrefEditor?.clear()
        locationSharedPrefEditor?.apply()
        locationSharedPrefEditor?.commit()
    }

    fun getStoredLocationData(): LocationData {
        val lat = locationSharedPref?.getString(latitude, null).toString()
        val long = locationSharedPref?.getString(longitude, null).toString()
        return LocationData(lat, long)
    }
}