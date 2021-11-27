package ru.subnak.easybike.domain.model

data class JourneyValue(
    val valueID: Int,
    val journeyID: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Double,
    val time: Long,
    val systemTime: Long,
    val accuracy: Double
)
