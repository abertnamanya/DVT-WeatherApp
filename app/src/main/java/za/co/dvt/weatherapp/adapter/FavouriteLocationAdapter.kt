package za.co.dvt.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.database.entity.FavouriteLocation

class FavouriteLocationAdapter :
    RecyclerView.Adapter<FavouriteLocationAdapter.FavouriteLocationViewHolder>() {
    private var favouriteLocations = emptyList<FavouriteLocation>()


    class FavouriteLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.findViewById(R.id.location)
        val locationLayout: ConstraintLayout =
            itemView.findViewById<ConstraintLayout>(R.id.location_layout)
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
        holder.locationLayout.setOnClickListener {

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