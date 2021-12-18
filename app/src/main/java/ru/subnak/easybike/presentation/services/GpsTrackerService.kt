package ru.subnak.easybike.presentation.ui.map

import android.annotation.SuppressLint
import android.app.*
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.subnak.easybike.R
import ru.subnak.easybike.presentation.ui.fragments.MapsFragment
import ru.subnak.easybike.presentation.utils.Constants
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
import java.security.Permissions
import javax.inject.Inject
import androidx.lifecycle.LifecycleService.NOTIFICATION_SERVICE as NOTIFICATION_SERVICE1


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class GpsTrackerService : LifecycleService() {

    var isFirstJourney = true

    private var isTimerEnabled = false

    private var timeStarted = 0L // Time when our service was started

    private var lapTime = 0L

    private var timeRun = 0L

    private var serviceKilled = false

    private lateinit var pendingIntent: PendingIntent

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder:NotificationCompat.Builder


    lateinit var curNotificationBuilder: NotificationCompat.Builder


    lateinit var fusedClient: FusedLocationProviderClient


    override fun onCreate() {
        super.onCreate()

        postInit() // Function to post empty values to our live data. (We created this function at bottom).

        // Initially we set curNotificationBuilder to baseNotificationBuilder to avoid lateinit not initialized exception
        curNotificationBuilder = baseNotificationBuilder

        isTracking.observe(this, Observer {
            // Function to get location of user when tracking is set to true and save it to pathPoints variable. (We created this function at bottom).
            updateLocationTracking(it)

            // Function to update the notification whenever we are tracking. (We created this function at bottom).
            updateNotificationTrackingState(it)
        })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstJourney) {
                        // Function to start this service. (We created this function at bottom).
                        startForegroundService()
                        isFirstJourney = false

                        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
                    } else {
                        // When we resume our service, we only want to continue the timer instead of restarting entire service.
                        beginTraining()

                        //Toast.makeText(this,"Service Resumed",Toast.LENGTH_SHORT).show()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService() // Function to pause our service. (We created this function at bottom).
                }
                ACTION_STOP_SERVICE -> {
                    stopService() // Function to stop or end our service. (We created this function at bottom).
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
                ) // Get the current location of user using the fused location provider client
            } else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val timeRunInSeconds = MutableLiveData<Long>()

        private const val GPS_ACTION = "GPS action"
        private const val ACTION_NAME = "Action name"
        private const val ACTION_STOP_TRACKING = "Action stop tracking"

    }

    // Function to kill our service
    private fun stopService() {
        serviceKilled = true
        isFirstJourney = true
        pauseService()
        postInit()
        stopForeground(true)
        stopSelf()
    }

    // Function to pause our service
    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {

        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            smallestDisplacement = 10.0F
        }


        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        fusedClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }
    

    private fun addNullPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addLocationPoint(location: Location?) {
        location?.let {
            // Get latitudes and longitudes of user's current location
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                // Add the position to end of our pathPoints variable
                last().add(pos)
                pathPoints.postValue(this)
            }
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
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInSeconds.postValue((timeRun + lapTime) / 1000)
                delay(1000)
            }
            // Add the lap time to total time
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

        // Start our service as a foreground service
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        // As time changes and our service is running, we update our notification's content
        timeRunInSeconds.observe(this, Observer {
            if (!serviceKilled) {
                val notification =
                    curNotificationBuilder.setContentText(TrackingObject.getFormattedStopTime(it))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })

    }
    private fun updateNotificationTrackingState(isTracking: Boolean) {

        // Set notification action text
        val notificationActionText = if (isTracking) "Pause" else "Resume"

        // Set intent with action according to isTracking variable
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

        // This piece of code helps in clearing the previous actions
        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        // When our service is running, we notify the notification with the required data
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







