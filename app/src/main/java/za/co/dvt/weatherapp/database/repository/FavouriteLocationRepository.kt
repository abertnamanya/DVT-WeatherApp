package za.co.dvt.weatherapp.database.repository

import androidx.lifecycle.LiveData
import za.co.dvt.weatherapp.database.dao.FavouriteLocationDao
import za.co.dvt.weatherapp.database.entity.FavouriteLocation

class FavouriteLocationRepository(private val favouriteLocationDao: FavouriteLocationDao) {

    val getFavouriteLocations: LiveData<List<FavouriteLocation>> =
        favouriteLocationDao.getFavouriteLocations()

    suspend fun saveFavouriteLocation(favouriteLocation: FavouriteLocation) {
        favouriteLocationDao.saveFavouriteLocation(favouriteLocation)
    }

    fun getFavouriteLocationByUuid(uuid: Int): FavouriteLocation =
        favouriteLocationDao.getFavouriteLocationByUuid(uuid)

    fun getCurrentUserLocationInfo(): FavouriteLocation =
        favouriteLocationDao.getCurrentUserLocationInfo()


    fun updateFavouriteLocationInfo(name: String, latitude: String, longitude: String) {
        favouriteLocationDao.updateFavouriteLocationInfo(name, latitude, longitude)
    }

}