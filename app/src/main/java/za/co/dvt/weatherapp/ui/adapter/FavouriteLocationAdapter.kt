package za.co.dvt.weatherapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.activities.WeatherForecastActivity
import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.utils.GeneralPrefs

class FavouriteLocationAdapter(context: Context) :
    RecyclerView.Adapter<FavouriteLocationAdapter.FavouriteLocationViewHolder>() {
    private var favouriteLocations = emptyList<FavouriteLocation>()
    private var selectLocationPref: GeneralPrefs
    private val context = context

    init {
        selectLocationPref = GeneralPrefs(context)
    }

    class FavouriteLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.findViewById(R.id.location)
        val locationLayout: RelativeLayout =
            itemView.findViewById(R.id.location_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteLocationViewHolder {
        return FavouriteLocationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.favourite_places_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouriteLocationViewHolder, position: Int) {
        val item = favouriteLocations[position]
        holder.place.text = item.name
        val location = FavouriteLocation(
            item.uuid,
            item.name,
            item.latitude,
            item.longitude,
            false
        )
        holder.locationLayout.setOnClickListener {
            selectLocationPref.storeActiveWeatherLocation(
                location
            )
            val intent = Intent(context, WeatherForecastActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favouriteLocations.size
    }

    fun setLLiveData(location: List<FavouriteLocation>) {
        this.favouriteLocations = location
        notifyDataSetChanged()
    }

}