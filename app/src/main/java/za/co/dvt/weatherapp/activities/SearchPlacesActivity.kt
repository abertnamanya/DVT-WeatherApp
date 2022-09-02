package za.co.dvt.weatherapp.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import za.co.dvt.locationtracker.LocationUtil
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.ui.viewModel.FavouriteLocationViewModel
import za.co.dvt.weatherapp.utils.CurrentFavouriteLocation
import za.co.dvt.weatherapp.utils.ToastNotification
import java.util.*

class SearchPlacesActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    var googleMap: GoogleMap? = null
    private lateinit var currentLatLng: LatLng
    private lateinit var selectedLocationInfo: SelectedLocationInfo
    private lateinit var favouriteLocationViewModel: FavouriteLocationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_places)
        supportActionBar?.apply {
            title = "Add Location"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        favouriteLocationViewModel =
            ViewModelProvider(this)[FavouriteLocationViewModel::class.java]

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        currentLatLng = LatLng(
            LocationUtil(this).useStoredLocationData().latitude.toDouble(),
            LocationUtil(this).useStoredLocationData().longitude.toDouble()
        )

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.UPDATED_GOOGLE_API_KEY), Locale.US);
        }
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val googleMapTitle = place.name
                val latLng = place.latLng
                updateGoogleMap(googleMapTitle, latLng)
            }

            override fun onError(status: Status) {
                Log.i("responseData", "An error occurred: $status")
            }
        })
    }

    private fun updateGoogleMap(markerTitle: String, markerPosition: LatLng) {
        googleMap?.clear()
        val options = MarkerOptions()
            .position(markerPosition)
            .title(markerTitle)

        googleMap?.apply {
            addMarker(options)
            moveCamera(CameraUpdateFactory.newLatLng(markerPosition))
            animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15f))
        }
        selectedLocationInfo = SelectedLocationInfo(
            markerPosition.latitude.toString(),
            markerPosition.longitude.toString(),
            markerTitle
        )

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap?.uiSettings?.apply {
            isZoomControlsEnabled = false
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }
        updateGoogleMap("Current", currentLatLng)
        map?.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        createConfirmDialog()
        return false
    }

    private fun createConfirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Favourite Location Option")
        builder.setMessage("Do you want to save ${selectedLocationInfo.place}")
        builder.setPositiveButton(R.string.OKAY) { dialog, _ ->
            if (selectedLocationInfo.latitude != null && selectedLocationInfo != null && selectedLocationInfo.place != null) {
                CurrentFavouriteLocation(favouriteLocationViewModel).addLocationToFavourites(
                    selectedLocationInfo.place,
                    selectedLocationInfo.latitude,
                    selectedLocationInfo.longitude,
                    false
                )
                ToastNotification(this).showToastNotification("${selectedLocationInfo.place} has been added to the Favourites")
                val intent = Intent(this, FavouriteLocationsActivity::class.java)
                startActivity(intent)
            } else {
                ToastNotification(this).showToastNotification("Place has not been added to the Favourites")
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.CANCEL) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

}

data class SelectedLocationInfo(
    var latitude: String,
    var longitude: String,
    var place: String
)

