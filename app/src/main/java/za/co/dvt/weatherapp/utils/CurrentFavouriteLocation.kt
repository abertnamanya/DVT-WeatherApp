package za.co.dvt.weatherapp.utils

import android.content.Context
import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.ui.viewModel.FavouriteLocationViewModel

class CurrentFavouriteLocation(context: Context) {
    fun addLocationToFavourites(
        favouriteLocationViewModel: FavouriteLocationViewModel,
        placeName: String,
        latitude: String,
        longitude: String
    ): Long {
        return favouriteLocationViewModel.saveFavouriteLocation(
            FavouriteLocation(
                0,
                placeName,
                latitude,
                longitude
            )
        )

    }

}