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

    fun getFavouriteLocationByUuid(uuid: Int) {
        favouriteLocationDao.getFavouriteLocationByUuid(uuid)
    }

}