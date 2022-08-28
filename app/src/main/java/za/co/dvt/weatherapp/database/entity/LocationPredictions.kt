package za.co.dvt.weatherapp.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class LocationPredictions(
    @Embedded val favouriteLocation: FavouriteLocation,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "favourite_location_id"
    ) val locationPredictions: List<WeatherPrediction>
)