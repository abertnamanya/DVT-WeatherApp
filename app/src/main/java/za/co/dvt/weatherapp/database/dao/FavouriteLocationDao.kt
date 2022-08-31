package za.co.dvt.weatherapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import za.co.dvt.weatherapp.database.entity.FavouriteLocation


@Dao
interface FavouriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveFavouriteLocation(favouriteLocation: FavouriteLocation)

    @Query("SELECT * FROM FavouriteLocation ORDER BY uuid DESC")
    fun getFavouriteLocations(): LiveData<List<FavouriteLocation>>

    @Query("SELECT * FROM FavouriteLocation where uuid=:uuid")
    fun getFavouriteLocationByUuid(uuid: Int): FavouriteLocation

    @Query("SELECT * FROM FavouriteLocation where is_current_location='1'")
    fun getCurrentUserLocationInfo(): FavouriteLocation


    @Query("UPDATE FavouriteLocation SET name=:name, latitude=:latitude, longitude=:longitude WHERE is_current_location = '1'")
    fun updateFavouriteLocationInfo(name: String, latitude: String, longitude: String)

    @Delete
    fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)
}