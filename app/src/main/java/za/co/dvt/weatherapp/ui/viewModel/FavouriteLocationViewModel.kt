package za.co.dvt.weatherapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.dvt.weatherapp.database.AppDatabase
import za.co.dvt.weatherapp.database.entity.FavouriteLocation
import za.co.dvt.weatherapp.database.repository.FavouriteLocationRepository

class FavouriteLocationViewModel(application: Application) : AndroidViewModel(application) {
    val favouriteLocations: LiveData<List<FavouriteLocation>>
    private val repository: FavouriteLocationRepository

    init {
        val favouriteLocationDao = AppDatabase.getInstance(application)?.FavouriteLocationDao()
        repository = FavouriteLocationRepository(favouriteLocationDao!!)
        favouriteLocations = repository.getFavouriteLocations
    }

    fun saveFavouriteLocation(favouriteLocation: FavouriteLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveFavouriteLocation(favouriteLocation)
        }
    }

    fun getFavouriteLocationByUuid(uuid: Int): FavouriteLocation =
        repository.getFavouriteLocationByUuid(uuid)

    fun getCurrentUserLocationInfo(): FavouriteLocation =
        repository.getCurrentUserLocationInfo()

    fun updateCurrentLocation(name: String, latitude: String, longitude: String) =
        repository.updateFavouriteLocationInfo(name, latitude, longitude)


}