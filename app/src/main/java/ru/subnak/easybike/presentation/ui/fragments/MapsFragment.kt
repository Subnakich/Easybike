package ru.subnak.easybike.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentMapsBinding
import ru.subnak.easybike.presentation.ui.map.GpsTrackerService
import ru.subnak.easybike.presentation.ui.map.Polyline
import ru.subnak.easybike.presentation.ui.viewmodels.MapViewModel
import ru.subnak.easybike.presentation.utils.Constants.ACTION_PAUSE_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.ACTION_STOP_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.DEFAULT_ZOOM
import ru.subnak.easybike.presentation.utils.Constants.MAP_ZOOM
import ru.subnak.easybike.presentation.utils.Constants.POLYLINE_COLOR
import ru.subnak.easybike.presentation.utils.Constants.POLYLINE_WIDTH
import ru.subnak.easybike.presentation.utils.TrackingObject
import ru.subnak.easybike.presentation.utils.TrackingObject.sumLengthOfPolylines
import java.util.*

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private var gMap: GoogleMap? = null

    private var isTracking = false

    private var pathPoints = mutableListOf<Polyline>()

    private var timeInSeconds = 0L

    private var distance = 0f

    private var speed = 0

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var lastKnownLocation: Location? = null


    val viewModel: MapViewModel by viewModels()


    private val callback = OnMapReadyCallback { _ ->

    }

    private var _binding: FragmentMapsBinding? = null
    private val binding: FragmentMapsBinding
        get() = _binding ?: throw RuntimeException("FragmentMapsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        subscribeToObservers()

        binding.mapView.getMapAsync {
            gMap =
                it // Get map asynchronously and assign the result to our map (google map instance) we created on top
            addAllPolylines()
        }

        binding.mapView.onCreate(savedInstanceState)

        binding.btStart.setOnClickListener {
            toggleJourney()
        }

        binding.btSave.setOnClickListener {
            zoomingUp()
            endJourneyAndSaveToDb()
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(gMap: GoogleMap) {
        this.gMap = gMap

        updateLocationUI()

        getDeviceLocation()

        gMap.setMyLocationEnabled(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(callback)


    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (gMap == null) {
            return
        }
        gMap?.isMyLocationEnabled = true
        gMap?.uiSettings?.isMyLocationButtonEnabled = true

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {

        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Set the map's camera position to the current location of the device.
                lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    gMap?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                lastKnownLocation!!.latitude,
                                lastKnownLocation!!.longitude
                            ), DEFAULT_ZOOM.toFloat()
                        )
                    )
                }
            }
        }
    }


    private fun subscribeToObservers() {

        GpsTrackerService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        GpsTrackerService.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
            val distTrack = it
            distance = sumLengthOfPolylines(distTrack)
            binding.tvDistance.text = "${Math.round((distance / 1000f) * 10) / 10} km"
        })

        GpsTrackerService.timeRunInSeconds.observe(viewLifecycleOwner, {
            timeInSeconds = it
            val formattedTime = TrackingObject.getFormattedStopTime(timeInSeconds)
            binding.tvTime.text = formattedTime

            speed =
                Math.round(((distance / timeInSeconds) * (3600 / 1000)) * 10) / 10 // Show speed in km/h
            binding.tvSpeed.text = "$speed kmh"

            if (timeInSeconds > 0L) {
                binding.btSave.visibility = View.VISIBLE
            }

        })


    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            gMap?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) { // If our distance line is not empty and last polyline contains a start and end point
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()

            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)

            gMap?.addPolyline(polylineOptions)

        }
    }


    private fun toggleJourney() {
        if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            binding.btStart.text = getString(R.string.btStartService)
        } else {
            binding.btStart.text = getString(R.string.btStopService)
        }
    }


    private fun zoomingUp() {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (pos in polyline) {
                bounds.include(pos)
            }
        }

        gMap?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05f).toInt()
            )
        )

    }

    private fun endJourneyAndSaveToDb() {
        gMap?.snapshot { bmp ->
            var distance = 0
            for (polyline in pathPoints) {
                distance += (TrackingObject.getPolylineLenght(polyline).toInt()) / 1000
            }

            // Current date and time
            val date = Calendar.getInstance().timeInMillis

            TODO("Create journey and add to db")


            // Save our journey object to database
            //viewModel.addJourney(journey)

            stopJourney()

        }
    }

    private fun stopJourney() {
        sendCommandToService(ACTION_STOP_SERVICE)
    }


    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            gMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }


    private fun sendCommandToService(action: String) =
        requireActivity().startService(Intent(context, GpsTrackerService::class.java).also {
            it.action = action
            requireActivity().startService(it)
        })

    // Following are the functions to handle the lifecycle of our map. Removing these functions may cause the app to crash.
    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


}