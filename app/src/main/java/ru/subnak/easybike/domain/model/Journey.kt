package ru.subnak.easybike.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Journey(
    val journeyID: Int,
    val userID: Int,
    val date: Long,
    val speed: Int,
    val distance: Double,
    val duration: Long,
    val img: String,
    val journeyValues: List<JourneyValue>
) : Parcelable