package ru.subnak.easybike.presentation.ui.map

import android.annotation.SuppressLint
import android.app.*
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.subnak.easybike.R
import ru.subnak.easybike.domain.model.JourneyValue
import ru.subnak.easybike.presentation.utils.Constants.ACTION_PAUSE_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.ACTION_STOP_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.FASTEST_LOCATION_INTERVAL
import ru.subnak.easybike.presentation.utils.Constants.LOCATION_UPDATE_INTERVAL
import ru.subnak.easybike.presentation.utils.Constants.NOTIFICATION_CHANNEL_ID
import ru.subnak.easybike.presentation.utils.Constants.NOTIFICATION_CHANNEL_NAME
import ru.subnak.easybike.presentation.utils.Constants.NOTIFICATION_ID
import ru.subnak.easybike.presentation.utils.PermissionsUtility
import ru.subnak.easybike.presentation.utils.TrackingObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class GpsTrackerService : LifecycleService() {

    var isFirstJourney = true

    private var isTimerEnabled = false

    private var timeStarted = 0L

    private var lapTime = 0L

    private var timeRun = 0L

    private var serviceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder:NotificationCompat.Builder


    lateinit var curNotificationBuilder: NotificationCompat.Builder




    override fun onCreate() {
        super.onCreate()

        postInit()

        curNotificationBuilder = baseNotificationBuilder

        isTracking.observe(this, Observer {

            updateLocationTracking(it)

            updateNotificationTrackingState(it)
        })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstJourney) {
                        startForegroundService()
                        isFirstJourney = false

                    } else {
                        beginTraining()

                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    stopService()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun postInit() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)

    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addLocationPoint(location)
                    }
                }
            }
        }

    }


    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (PermissionsUtility.hasLocationPermission(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val timeRunInSeconds = MutableLiveData<Long>()
        val points = MutableLiveData<MutableList<JourneyValue>>()

    }

    private fun stopService() {
        serviceKilled = true
        isFirstJourney = true
        pauseService()
        postInit()
        stopForeground(true)
        stopSelf()

    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }



    private fun addNullPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addNullJourneyValue() = points.value?.apply {
        add(JourneyValue(0.0,0.0,0.0,0f,0L,0L,0f,0,0))
        points.postValue(this)
    } ?: points.postValue(mutableListOf())

    private fun addLocationPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
            val systemTime =  Calendar.getInstance().timeInMillis
            val journeyValue = JourneyValue(
                location.latitude,
                location.longitude,
                location.altitude,
                location.speed,
                location.time,
                systemTime,
                location.accuracy
            )
            val value = points.value
            value?.add(journeyValue)
            points.value = value!!
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }


    private fun beginTraining() {
        addNullPolyline()
        addNullJourneyValue()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInSeconds.postValue((timeRun + lapTime) / 1000)
                delay(1000)
            }
            timeRun += lapTime
        }
    }


    private fun startForegroundService() {

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        isTracking.postValue(true)

        beginTraining()

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this, Observer {
            if (!serviceKilled) {
                val notification =
                    curNotificationBuilder.setContentText(TrackingObject.getFormattedStopTime(it))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })

    }
    private fun updateNotificationTrackingState(isTracking: Boolean) {

        val notificationActionText = if (isTracking) "Pause" else "Resume"

        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, GpsTrackerService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, GpsTrackerService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if (!serviceKilled) {
            curNotificationBuilder = baseNotificationBuilder.addAction(
                R.drawable.ic_bike,
                notificationActionText,
                pendingIntent
            )
            notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }


}







