package ru.subnak.easybike.domain.journeyvalues

data class JourneyValues(
    val ValueID: Int,
    val JourneyID: Int,
    val Latitude: Double,
    val Longitude: Double,
    val Altitude: Double,
    val Speed: Double,
    val Time: Long,
    val SystemTime: Long,
    val Accuracy: Double
)
