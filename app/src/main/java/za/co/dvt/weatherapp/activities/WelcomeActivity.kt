package za.co.dvt.weatherapp.activities

import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import za.co.dvt.locationtracker.LocationUpdatesService
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.api.Resource
import za.co.dvt.weatherapp.api.RetrofitService
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
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var startBtn: Button
    private lateinit var viewModel: WeatherApiViewModel
    private val retrofitService = RetrofitService.forecastApi
    private lateinit var placeName: TextView
    private lateinit var favouriteLocationViewModel: FavouriteLocationViewModel

    private var mService: LocationUpdatesService? = null
    private var mBound: Boolean = false
    private var locationName = "Current Location"
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
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

        startBtn = findViewById<Button>(R.id.start_btn)
        startBtn.visibility = View.GONE

        startBtn.setOnClickListener {
            /**
             * first time usage of the app indicate sharedPref
             */
            GeneralPrefs(this).userInitialAppUsage()

            /**
             *  save current location to favourite locations
             */
            CurrentFavouriteLocation(favouriteLocationViewModel).addLocationToFavourites(
                locationUtil.useStoredLocationData().placeName,
                locationUtil.useStoredLocationData().latitude,
                locationUtil.useStoredLocationData().longitude,
                true
            )
            /**
             * fetch current user location info and add to the shared Pref
             */
            val favouriteLocation: FavouriteLocation =
                CurrentFavouriteLocation(favouriteLocationViewModel).getCurrentUserLocationInfo()
            GeneralPrefs(this).storeActiveWeatherLocation(
                FavouriteLocation(
                    favouriteLocation.uuid,
                    favouriteLocation.name,
                    favouriteLocation.latitude,
                    favouriteLocation.longitude,
                    true
                )
            )
            /**
             * launch weather statistics activity
             */
            val intent = Intent(this, WeatherForecastActivity::class.java)
            startActivity(intent)
            finish()

        }
        /**
         *  listen for location broadcast from the service
         */
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                when (intent?.action) {
                    "LocationUpdates" -> {
                        try {
                            val latitude = intent.getStringExtra("current_location_latitude")
                            val longitude = intent.getStringExtra("current_location_longitude")
                            if (latitude != null && longitude != null) {
                                locationUtil.handleLocationSharedPref(
                                    latitude,
                                    longitude,
                                    locationName
                                )
                                getWeatherPredictions()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

    }

    /**
     * check if the location permission have been allowed and enabled
     */
    override fun onStart() {
        super.onStart()
        if (locationUtil.isLocationPermissionsEnabled()) {
            if (locationUtil.isLocationEnabled()) {
                initializeService()
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            locationUtil.requestLocationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        /**
         *  avoid the app from coming back to this activity this
         *  its the initial activity after installation
         */
        if (GeneralPrefs(this).isFirstTimeUsage() == true) {
            val intent = Intent(this, WeatherForecastActivity::class.java)
            startActivity(intent)
            finish()
        }
        /**
         * re-request for location updates on resume
         */
        val intentFilter = IntentFilter()
        intentFilter.addAction("LocationUpdates")
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(it, intentFilter)
        }

    }

    override fun onPause() {
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        super.onPause()
    }

    /**
     *  get weather predictions from the open weather api
     */
    private fun getWeatherPredictions() {
        viewModel.getWeeklyWeatherInformation(
            true,
            Constants.weatherTempUnits,
            Constants.openWeatherApiKey
        )
        viewModel.weatherInfo.observe(this) {
            when (it) {
                is Resource.Success<*> -> {
                    /**
                     * update the UI elements after the data coming from
                     *  lifecycle live data
                     */
                    locationName = it.data?.city?.name.toString()
                    placeName.text = locationName
                    placeName.visibility = View.VISIBLE
                    loadingProgressBar.visibility = View.GONE
                    startBtn.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    it.message?.let { message ->
                        startBtn.visibility = View.GONE
                        loadingProgressBar.visibility = View.GONE
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationUtil.locationPermissionRequestId) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (locationUtil.isLocationPermissionsEnabled()) {
                    if (locationUtil.isLocationEnabled()) {
                        initializeService()
                    } else {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                } else {
                    locationUtil.requestLocationPermission()
                }
            }
        }
    }

    private var mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: LocationUpdatesService.LocalBinder =
                service as LocationUpdatesService.LocalBinder
            mService = binder.service
            mBound = true
            mService?.requestLocationUpdates()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mBound = false
        }
    }

    private fun initializeService() {
        bindService(
            Intent(this, LocationUpdatesService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        if (mBound) {
            unbindService(mServiceConnection)
            mBound = false
        }
        super.onStop()
    }

}