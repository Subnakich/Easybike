package ru.subnak.easybike.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "journey_values",
    foreignKeys = [
        ForeignKey(
            entity = JourneyDbModel::class,
            parentColumns = ["id"],
            childColumns = ["journey_id"]
        )
    ]
)
data class JourneyValueDbModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val valueID: Int,
    @ColumnInfo(name = "journey_id", index = true)
    val journeyID: Int,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val speed: Double = 0.0,
    val time: Long = 0L,
    val systemTime: Long = 0L,
    val accuracy: Double = 0.0
)
