package ru.subnak.easybike.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import ru.subnak.easybike.presentation.ui.map.Polyline
import ru.subnak.easybike.presentation.ui.map.Polylines

object TrackingObject {



    fun getFormattedStopTime(sec: Long): String {


        val hours = sec / 3600
        val minutes = sec / 60 % 60
        val seconds = sec % 60

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"

    }

    fun getPolylineLenght(polyline: Polyline): Float {
        var distance = 0f
        for (i in 0..polyline.size - 2) {
            val result = FloatArray(1)
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun sumLengthOfPolylines(polylines: Polylines): Float {
        var totalDistance = 0f
        for (i in 0..polylines.size - 1) {
            totalDistance += getPolylineLenght(polylines[i])
        }
        return totalDistance
    }


}
