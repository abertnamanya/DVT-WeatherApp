package za.co.dvt.weatherapp.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.api.Resource
import za.co.dvt.weatherapp.api.RetrofitService
import za.co.dvt.weatherapp.api.WeeklyWeatherDataResponse
import za.co.dvt.weatherapp.api.repository.WeatherApiRepository
import za.co.dvt.weatherapp.api.viewModel.WeatherApiViewModel
import za.co.dvt.weatherapp.api.viewModel.WeatherApiViewModelFactory
import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.ui.viewModel.FavouriteLocationViewModel
import za.co.dvt.weatherapp.utils.Constants
import za.co.dvt.weatherapp.utils.CurrentFavouriteLocation
import za.co.dvt.weatherapp.utils.GeneralPrefs

class WelcomeActivity : AppCompatActivity() {
    lateinit var locationUtil: LocationUtil
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var loadingProgressBar: ProgressBar
    private lateinit var viewModel: WeatherApiViewModel
    private val retrofitService = RetrofitService.forecastApi
    private lateinit var placeName: TextView
    private var locationName: String = "Current"
    private lateinit var favouriteLocationViewModel: FavouriteLocationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationUtil = LocationUtil(this)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        loadingProgressBar.visibility = View.VISIBLE
        placeName = findViewById(R.id.place_name)
        placeName.visibility = View.GONE
        viewModel =
            ViewModelProvider(
                this,
                WeatherApiViewModelFactory(WeatherApiRepository(retrofitService), this)
            )[WeatherApiViewModel::class.java]

        favouriteLocationViewModel =
            ViewModelProvider(this)[FavouriteLocationViewModel::class.java]

        val startBtn = findViewById<Button>(R.id.start_btn)

        startBtn.setOnClickListener {
            GeneralPrefs(this).userInitialAppUsage()
            //save current location to favourite locations
            val uuid: Long = CurrentFavouriteLocation(this).addLocationToFavourites(
                favouriteLocationViewModel,
                locationName,
                locationUtil.useStoredLocationData().latitude,
                locationUtil.useStoredLocationData().longitude
            )
            if (uuid != null) {
                GeneralPrefs(this).storeActiveWeatherLocation(
                    FavouriteLocation(
                        uuid.toInt(),
                        locationName,
                        locationUtil.useStoredLocationData().latitude,
                        locationUtil.useStoredLocationData().longitude
                    )
                )
                val intent = Intent(this, WeatherForecastActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        locationUtil.getLastKnownLocation(fusedLocationClient)
        if (locationUtil.useStoredLocationData().latitude != null && locationUtil.useStoredLocationData().latitude != null) {
            getWeatherPredictions()
        }
    }

    private fun getWeatherPredictions() {
        viewModel.getWeeklyWeatherInformation(
            true,
            Constants.weatherTempUnits,
            Constants.openWeatherApiKey
        )
        viewModel.weatherInfo.observe(this) {
            when (it) {
                is Resource.Success<*> -> {
                    locationName = it.data?.city?.name.toString()
                    placeName.text = locationName
                    placeName.visibility = View.VISIBLE
                    loadingProgressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        loadingProgressBar.visibility = View.GONE
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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
                if (locationUtil.useStoredLocationData().latitude != null && locationUtil.useStoredLocationData().latitude != null) {
                    getWeatherPredictions()
                }
            }
        }
    }

}