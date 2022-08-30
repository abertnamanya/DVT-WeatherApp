package za.co.dvt.weatherapp.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.api.*
import za.co.dvt.weatherapp.api.repository.WeatherApiRepository
import za.co.dvt.weatherapp.utils.Constants
import za.co.dvt.weatherapp.api.viewModel.WeatherApiViewModel
import za.co.dvt.weatherapp.api.viewModel.WeatherApiViewModelFactory
import za.co.dvt.weatherapp.database.entity.WeatherPrediction
import za.co.dvt.weatherapp.ui.adapter.WeatherAdapter
import za.co.dvt.weatherapp.ui.viewModel.WeatherViewModel
import za.co.dvt.weatherapp.utils.GeneralPrefs

class WeatherForecastActivity : AppCompatActivity() {
    private var locationUtil: LocationUtil? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherApiViewModel
    private val retrofitService = RetrofitService.forecastApi
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        val navIcon = findViewById<ImageView>(R.id.nav_icon)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationUtil = LocationUtil(this)
        viewModel =
            ViewModelProvider(
                this,
                WeatherApiViewModelFactory(WeatherApiRepository(retrofitService), this)
            )[WeatherApiViewModel::class.java]

        viewModel.getWeeklyWeatherInformation(
            false,
            Constants.weatherTempUnits,
            Constants.openWeatherApiKey
        )
        navIcon.setOnClickListener {
            populateMenuOptions()
        }
        adapter = WeatherAdapter(this)
        val weeklyForecastRecycleView =
            findViewById<RecyclerView>(R.id.weekly_forecast_recycleView)
        weeklyForecastRecycleView.adapter = adapter

        weatherViewModel =
            ViewModelProvider(this)[WeatherViewModel::class.java]

        weeklyForecastRecycleView.layoutManager = LinearLayoutManager(this)
        weatherViewModel.locationWeatherPredictions.observe(this, Observer { prediction ->
            adapter.updateLiveData(prediction)
        })

        updateUIComponents()
    }

    private fun updateUIComponents() {
        val weatherDegrees = findViewById<TextView>(R.id.weather_degrees)
        val conditions = findViewById<TextView>(R.id.conditions)
        val minimumTemp = findViewById<TextView>(R.id.min_temp)
        val currentTemp = findViewById<TextView>(R.id.current_temp)
        val maximumTemp = findViewById<TextView>(R.id.max_temp)

        /**
         *  current selected place name
         */
        val currentPlaceName = findViewById<TextView>(R.id.CurrentPlaceName)
        currentPlaceName.text = GeneralPrefs(this).getActiveWeatherLocation().name

        val currentPredictionObserver = Observer<WeatherPrediction> { prediction ->

            /**
             * update UI elements values
             */
            if (prediction != null) {
                weatherDegrees.text = prediction.temp.toString().plus('\u00B0')
                conditions.text = prediction.weather
                minimumTemp.text = prediction.temp_min.toString().plus('\u00B0')
                currentTemp.text = prediction.temp.toString().plus('\u00B0')
                maximumTemp.text = prediction.temp_max.toString().plus('\u00B0')

                /**
                 *  change layout background
                 */
                val statisticsHeaderConstrainLayout =
                    findViewById<ConstraintLayout>(R.id.statisticsHeaderConstrainLayout)

                val statisticsBodyConstrainLayout =
                    findViewById<ConstraintLayout>(R.id.statisticsBodyConstrainLayout)

                when (prediction.weather) {
                    "Rain" -> {
                        statisticsHeaderConstrainLayout.setBackgroundResource(R.drawable.sea_rainy)
                        statisticsBodyConstrainLayout.setBackgroundColor(
                            ContextCompat.getColor(this, R.color.rainy_body_bg)
                        )
                    }
                    "Clear" -> {
                        statisticsHeaderConstrainLayout.setBackgroundResource(R.drawable.sea_sunny)
                        statisticsBodyConstrainLayout.setBackgroundColor(
                            ContextCompat.getColor(this, R.color.sunny_body_bg)
                        )
                    }
                    "Clouds" -> {
                        statisticsHeaderConstrainLayout.setBackgroundResource(R.drawable.sea_cloudy)
                        statisticsBodyConstrainLayout.setBackgroundColor(
                            ContextCompat.getColor(this, R.color.cloudy_body_bg)
                        )
                    }
                }
            }
        }
        weatherViewModel.currentWeatherPredictions.observe(this, currentPredictionObserver)

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
            } else if (options[it] == "Favourite Locations Map") {
                val intent = Intent(this, FavouriteLocationsMapActivity::class.java)
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

