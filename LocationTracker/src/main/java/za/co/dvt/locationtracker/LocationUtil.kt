package za.co.dvt.locationtracker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.provider.Settings
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class LocationUtil(private val context: Context) {

    val locationPermissionId = 1003
    private fun locationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun locationPermissionsEnabled(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            locationPermissionId
        )
    }

    fun getLastKnownLocation(fusedLocationClient: FusedLocationProviderClient) {
        if (locationPermissionsEnabled()) {
            if (locationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationPermission()
                }
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location == null) {
                        } else {
                            LocationSharedPref(context).removePreviousLocationData()
                            val locationData = LocationData(location.latitude.toString(), location.longitude.toString())
                            LocationSharedPref(context).storeCurrentLocation(locationData)
                        }
                    }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
        } else {
            requestLocationPermission()
        }
    }

    fun useStoredLocationData(): LocationData {
        return LocationSharedPref(context).getStoredLocationData()
    }
}

