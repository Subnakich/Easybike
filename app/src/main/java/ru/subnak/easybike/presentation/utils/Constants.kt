package ru.subnak.easybike.presentation.utils

import android.graphics.Color

object Constants {

    const val UNDEFINED_ID = 0

    const val MODE_EDIT = "mode_edit"
    const val MODE_ADD = "mode_add"

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY"

    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "1"
    const val NOTIFICATION_CHANNEL_NAME = "EasyBike"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.GREEN
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 17.5f

}