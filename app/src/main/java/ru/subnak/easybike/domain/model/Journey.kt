package ru.subnak.easybike.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Journey(
    val userID: Int,
    val date: Long,
    val speed: Int,
    val distance: Double,
    val duration: Long,
    val img: String,
    val journeyValues: List<JourneyValue>,
    var journeyID: Int = UNDEFINED_ID
) : Parcelable {

    companion object {

        const val UNDEFINED_ID = 0
    }
}