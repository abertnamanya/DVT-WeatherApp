package za.co.dvt.weatherapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.databinding.ActivityFavouriteLocationsBinding
import za.co.dvt.weatherapp.ui.adapter.FavouriteLocationAdapter
import za.co.dvt.weatherapp.ui.viewModel.FavouriteLocationViewModel

class FavouriteLocationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteLocationsBinding
    private lateinit var favouriteLocationViewModel: FavouriteLocationViewModel
    private lateinit var adapter: FavouriteLocationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavouriteLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Favourite Locations"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        favouriteLocationViewModel =
            ViewModelProvider(this)[FavouriteLocationViewModel::class.java]


        adapter = FavouriteLocationAdapter(this)

        val placesRecycleView =
            findViewById<RecyclerView>(R.id.favourite_places_recycleView)
        placesRecycleView.adapter = adapter
        placesRecycleView.layoutManager = LinearLayoutManager(this)
        favouriteLocationViewModel.favouriteLocations.observe(this, Observer { location ->
            adapter.setLLiveData(location)
        })

        binding.addFavouriteLocFab.setOnClickListener {
            val intent = Intent(this, SearchPlacesActivity::class.java)
            startActivity(intent)
        }
    }

}