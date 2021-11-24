package ru.subnak.easybike.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "journey_values",
    foreignKeys = [
        ForeignKey(entity = JourneyDbModel::class, parentColumns = ["id"], childColumns = ["journey_id"])
    ]
)
data class JourneyValueDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val valueID: Int,
    @ColumnInfo(name = "journey_id")
    val journeyID: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Double,
    val time: Long,
    val systemTime: Long,
    val accuracy: Double
)
