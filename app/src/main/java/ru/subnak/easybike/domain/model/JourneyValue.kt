package ru.subnak.easybike.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JourneyValue(
    val journeyID: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Double,
    val time: Long,
    val systemTime: Long,
    val accuracy: Double,
    var valueID: Int = UNDEFINED_ID
) : Parcelable {

    companion object {

        const val UNDEFINED_ID = 0
    }
}
