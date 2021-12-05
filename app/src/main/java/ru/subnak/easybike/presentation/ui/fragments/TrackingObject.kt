package ru.subnak.easybike.presentation.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.Size
import androidx.core.content.ContextCompat

object TrackingObject {

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun getFormattedStopTime(sec: Long): String {


        val hours = sec / 3600
        val minutes = sec / 60 % 60
        val seconds = sec % 60

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"

    }

    fun getPolylineLenght(polyline: Polyline): Float {
        var distation = 0f
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
            distation += result[0]
        }
        return distation
    }

    fun sumLengthOfPolylines(polylines: Polylines): Float {
        var totalDistation = 0f
        for (i in 0..polylines.size - 1) {
            totalDistation += getPolylineLenght(polylines[i])
        }
        return totalDistation
    }

    private fun hasPermissions(
        context: Context,
        @Size(min = 1) vararg perms: String
    ): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w("PERMISSIONS", "Android version < 6")

            return true
        }

        requireNotNull(context) { "Can't check permissions for null context" }
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


}
