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

    @Delete
    fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)
}