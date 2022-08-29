package za.co.dvt.weatherapp.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonArray
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.api.*
import za.co.dvt.weatherapp.data.WeeklyWeatherDataResponse
import za.co.dvt.weatherapp.utils.Constants

class WeatherForecastActivity : AppCompatActivity() {
    private var locationUtil: LocationUtil? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherViewModel
    private val retrofitService = RetrofitService.forecastApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        val navIcon = findViewById<ImageView>(R.id.nav_icon)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationUtil = LocationUtil(this)
        viewModel =
            ViewModelProvider(
                this,
                WeatherViewModelFactory(WeatherRepository(retrofitService), this)
            )[WeatherViewModel::class.java]

        viewModel.getWeeklyWeatherInformation(
            locationUtil!!.useStoredLocationData().latitude,
            locationUtil!!.useStoredLocationData().longitude,
            Constants.weatherTempUnits,
            Constants.openWeatherApiKey
        )
        navIcon.setOnClickListener {
            populateMenuOptions()
        }
        observeUI()
    }

    private fun observeUI() {
        viewModel.weatherInfo.observe(this) {
            when (it) {
                is Resource.Success<*> -> {
                    val data: WeeklyWeatherDataResponse? =it.data
//                    Log.d("DataFrom", it.data.)
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        Log.d("DataFrom", message.toString())
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
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

