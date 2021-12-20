package ru.subnak.easybike.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val distance: Float,
    val duration: Long,
    val img: String
)
