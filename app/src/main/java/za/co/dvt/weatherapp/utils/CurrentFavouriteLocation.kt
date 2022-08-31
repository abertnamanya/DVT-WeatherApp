package za.co.dvt.weatherapp.utils

import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.ui.viewModel.FavouriteLocationViewModel

class CurrentFavouriteLocation(favouriteLocationViewModel: FavouriteLocationViewModel) {
    private val favouriteLocationViewModel = favouriteLocationViewModel

    fun addLocationToFavourites(
        placeName: String,
        latitude: String,
        longitude: String,
        isCurrentLocation: Boolean
    ) {
        favouriteLocationViewModel.saveFavouriteLocation(
            FavouriteLocation(
                0,
                placeName,
                latitude,
                longitude,
                isCurrentLocation
            )
        )

    }

    fun getCurrentUserLocationInfo(): FavouriteLocation =
        favouriteLocationViewModel.getCurrentUserLocationInfo()

}