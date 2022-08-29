package za.co.dvt.weatherapp.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.utils.GeneralPrefs

class WelcomeActivity : AppCompatActivity() {
    lateinit var locationUtil: LocationUtil
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationUtil = LocationUtil(this)
        loadingProgressBar = findViewById<ProgressBar>(R.id.loadingProgressBar)
        val startBtn = findViewById<Button>(R.id.start_btn)

        startBtn.setOnClickListener {
            GeneralPrefs(this).userInitialAppUsage()
            val intent = Intent(this, WeatherForecastActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getCurrentWeatherData() {
//        val latitude = locationUtil.useStoredLocationData().latitude
//        val longitude = locationUtil.useStoredLocationData().longitude
//        if (latitude != null && longitude != null) {
//            val call = WeatherRequestCall().dailyWeatherForeCast(latitude, longitude)
//            if (call!!.isSuccessful || call.code() == 200) {
//
//            }
//        }

    }

    override fun onStart() {
        super.onStart()
        locationUtil.getLastKnownLocation(fusedLocationClient)
//        getCurrentWeatherData()
    }

    override fun onResume() {
        super.onResume()
        if (GeneralPrefs(this).isFirstTimeUsage() == true) {
            val intent = Intent(this, WeatherForecastActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationUtil.locationPermissionId) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationUtil.getLastKnownLocation(fusedLocationClient)
//                getCurrentWeatherData()
            }
        }
    }

}