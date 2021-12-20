package ru.subnak.easybike.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JourneyValue(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Float,
    val time: Long,
    val systemTime: Long,
    val accuracy: Float,
    var valueID: Int = UNDEFINED_ID,
    val journeyID: Int = UNDEFINED_ID
) : Parcelable {

    companion object {

        const val UNDEFINED_ID = 0
    }
}
