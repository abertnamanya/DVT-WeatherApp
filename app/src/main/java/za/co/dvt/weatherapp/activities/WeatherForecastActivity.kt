package za.co.dvt.weatherapp.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R

class WeatherForecastActivity : AppCompatActivity() {
    private var locationUtil: LocationUtil? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationUtil = LocationUtil(this)
        val navIcon = findViewById<ImageView>(R.id.nav_icon)
        navIcon.setOnClickListener {
            populateMenuOptions()
        }

    }

    private fun populateMenuOptions() {
        val options = arrayOf("Manage Locations", "Favourite Locations Map")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Choose Option")
        mBuilder.setSingleChoiceItems(options, -1) { dialogInterface, it ->
            dialogInterface.dismiss()
            if (options[it] == "Manage Locations") {
                val intent = Intent(this, FavouriteLocationsActivity::class.java)
                startActivity(intent)
            }
        }

        mBuilder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

    override fun onStart() {
        super.onStart()
        locationUtil?.getLastKnownLocation(fusedLocationClient)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationUtil?.locationPermissionId) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationUtil?.getLastKnownLocation(fusedLocationClient)
            }
        }
    }

}

