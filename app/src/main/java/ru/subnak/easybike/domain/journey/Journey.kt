package ru.subnak.easybike.domain.journey

import ru.subnak.easybike.domain.journeyvalues.JourneyValue

data class Journey(
    val journeyID: Int,
    val userID: Int,
    val date: Long,
    val speed: Int,
    val distance: Double,
    val duration: Long,
    val img: String,
    val journeyValues: List<JourneyValue>
)