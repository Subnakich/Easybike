package ru.subnak.easybike.domain.journey

import android.graphics.Bitmap

data class Journey(
    val JourneyID: Int,
    val UserID: Int,
    val Date: Long,
    val Speed: Int,
    val Distance: Double,
    val Duration: Long,
    val Img: Bitmap
)
