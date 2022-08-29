package za.co.dvt.weatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.dvt.weatherapp.R
import za.co.dvt.weatherapp.database.entity.WeatherPrediction
import za.co.dvt.weatherapp.utils.dayOfTheWeek

class WeatherAdapter(context: Context) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private var weatherPrediction = emptyList<WeatherPrediction>()


    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfTheWeek: TextView = itemView.findViewById(R.id.dayOfTheWeek)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        val temperatureReadings: TextView = itemView.findViewById(R.id.temperatureReadings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_weather_conditions, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = weatherPrediction[position]
        val convertToDayOfTheWeek = dayOfTheWeek(item.weather_date)
        holder.dayOfTheWeek.text = convertToDayOfTheWeek
        holder.temperatureReadings.text = item.temp.toString()
    }

    override fun getItemCount(): Int {
        return weatherPrediction.size
    }

    fun updateLiveData(prediction: List<WeatherPrediction>) {
        this.weatherPrediction = prediction
        notifyDataSetChanged()
    }
}