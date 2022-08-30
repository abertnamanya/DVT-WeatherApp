package za.co.dvt.weatherapp.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.ui.viewModel.FavouriteLocationViewModel
import za.co.dvt.weatherapp.utils.GeneralPrefs

class FavouriteLocationsMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    var googleMap: GoogleMap? = null
    private lateinit var favouriteLocationViewModel: FavouriteLocationViewModel
    private lateinit var selectedLocationInfo: SelectedLocationInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_locations_map)
        supportActionBar?.apply {
            title = "Favourite Locations"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        favouriteLocationViewModel =
            ViewModelProvider(this)[FavouriteLocationViewModel::class.java]

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMp: GoogleMap) {
        googleMap = googleMp
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap?.uiSettings?.apply {
            isZoomControlsEnabled = false
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }
        updateGoogleMap()
        googleMap?.setOnMarkerClickListener(this)
    }

    private fun updateGoogleMap() {
        googleMap?.clear()
        favouriteLocationViewModel.favouriteLocations.observe(this, Observer { location ->
            if (location != null) {
                for (favouritePlace in location) {
                    val currentLatLng = LatLng(
                        favouritePlace.latitude.toDouble(),
                        favouritePlace.longitude.toDouble()
                    )
                    val options = MarkerOptions()
                        .position(currentLatLng)
                        .snippet(favouritePlace.uuid.toString())
                        .title(favouritePlace.name)

                    googleMap?.apply {
                        addMarker(options)
                        moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                        animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }
                }
            }
        })
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        makeLocationActiveOnCurrentWeather(marker)
        return false
    }

    private fun makeLocationActiveOnCurrentWeather(marker: Marker) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Favourite Location Option")
        builder.setMessage("Do you want view weather predictions for ${marker.title} ")
        builder.setPositiveButton(R.string.OKAY) { dialog, _ ->
            GeneralPrefs(this).storeActiveWeatherLocation(
                FavouriteLocation(
                    marker.snippet!!.toInt(),
                    marker.title!!,
                    marker.position.latitude.toString(),
                    marker.position.longitude.toString()
                )
            )
            val intent = Intent(this, WeatherForecastActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.CANCEL) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}