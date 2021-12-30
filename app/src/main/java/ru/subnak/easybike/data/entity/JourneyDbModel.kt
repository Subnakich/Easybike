package ru.subnak.easybike.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.subnak.easybike.data.Converters

@Entity(
    tableName = "journeys",
)
data class JourneyDbModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val journeyID: Int,
    @ColumnInfo(name = "user_id")
    val userID: Int,
    val date: Long,
    val speed: Int,
    val distance: Double,
    val duration: Long,
    val img: Bitmap? = null
)
