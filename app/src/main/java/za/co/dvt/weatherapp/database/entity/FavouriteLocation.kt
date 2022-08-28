package za.co.dvt.weatherapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteLocation(
    @PrimaryKey(autoGenerate = true) val uuid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "latitude") val latitude: String,
    @ColumnInfo(name = "longitude") val longitude: String,
)
