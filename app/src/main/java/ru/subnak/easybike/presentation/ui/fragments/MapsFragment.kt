package ru.subnak.easybike.presentation.ui.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentMapsBinding
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.model.JourneyValue
import ru.subnak.easybike.presentation.ui.map.GpsTrackerService
import ru.subnak.easybike.presentation.ui.map.Polyline
import ru.subnak.easybike.presentation.ui.viewmodels.MapViewModel
import ru.subnak.easybike.presentation.utils.Constants
import ru.subnak.easybike.presentation.utils.Constants.ACTION_PAUSE_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.ACTION_STOP_SERVICE
import ru.subnak.easybike.presentation.utils.Constants.MAP_ZOOM
import ru.subnak.easybike.presentation.utils.Constants.POLYLINE_COLOR
import ru.subnak.easybike.presentation.utils.Constants.POLYLINE_WIDTH
import ru.subnak.easybike.presentation.utils.TrackingObject
import ru.subnak.easybike.presentation.utils.TrackingObject.sumLengthOfPolylines
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt
import com.google.android.gms.maps.CameraUpdateFactory

import androidx.lifecycle.Transformations.map

import com.google.android.gms.maps.model.LatLng
import android.location.Criteria
import android.location.Location

import androidx.core.content.ContextCompat.getSystemService

import android.location.LocationManager








@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {



    private var gMap: GoogleMap? = null

    private var isTracking = false

    private var pathPoints = mutableListOf<Polyline>()

    private var points = mutableListOf<JourneyValue>()

    private var timeInSeconds = 0L


    private var distance = 0f

    private var speed = 0

    private var mCurrLocationMarker: Marker? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private lateinit var viewModel: MapViewModel


    private val callback = OnMapReadyCallback { gMap ->
        this.gMap = gMap
        setMarker()
        zoomCamera()
    }


    private var _binding: FragmentMapsBinding? = null
    private val binding: FragmentMapsBinding
        get() = _binding ?: throw RuntimeException("FragmentMapsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        subscribeToObservers()



        binding.mapView.getMapAsync {
            gMap =
                it // Get map asynchronously and assign the result to our map (google map instance) we created on top
            addAllPolylines()
        }

        binding.mapView.onCreate(savedInstanceState)

        binding.btMyLocation.setOnClickListener{
            zoomCamera()
        }

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
        gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        gMap.uiSettings.isMapToolbarEnabled = true
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)

    }


    @SuppressLint("MissingPermission")
    private fun subscribeToObservers() {

        GpsTrackerService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })


        GpsTrackerService.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            addLatestPolyline()
            zoomCamera()
            val distTrack = it
            distance = sumLengthOfPolylines(distTrack)
            binding.tvDistance.text = "${Math.round((distance / 1000f) * 10) / 10} km"
        })

        GpsTrackerService.points.observe(viewLifecycleOwner, {
            points = it
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

    private fun getSpeed(): Int {
        speed =
            ((((distance / timeInSeconds) * (3600 / 1000)) * 10) / 10).roundToInt()
        return speed
    }

    @SuppressLint("MissingPermission")
    private fun setMarker(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 2000 // 3 seconds interval

        mLocationRequest.fastestInterval = 2000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()!!
        )

    }

    @SuppressLint("MissingPermission")
    private fun zoomCamera(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val position = CameraPosition.Builder()
                    .target(LatLng(it.latitude, it.longitude))
                    .zoom(MAP_ZOOM)
                    .build()
                gMap?.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            }
        }
    }





    var mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                    mCurrLocationMarker?.remove()
                val latLng = LatLng(location.latitude, location.longitude)
                currentUserPositionMarker(latLng)
            }
        }
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

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun currentUserPositionMarker(lastLatLng: LatLng) {

        val markerOptions = MarkerOptions()
            .position(lastLatLng)
            .title(getString(R.string.current_location))
            .icon(bitmapDescriptorFromVector(requireActivity(), R.drawable.ic_marker))
        mCurrLocationMarker = gMap?.addMarker(markerOptions)!!
//        val cameraPosition = CameraPosition.Builder()
//            .target(lastLatLng)
//            .zoom(MAP_ZOOM)
//            .build()
//        gMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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
                (binding.mapView.height * 0.1f).toInt()
            )
        )

    }

    private fun endJourneyAndSaveToDb() {
        gMap?.snapshot { bmp ->
            val distanceFin = floor(distance) / 1000.0
            val date = Calendar.getInstance().timeInMillis
            val journey = Journey(
                Constants.UNDEFINED_ID,
                date,
                getSpeed(),
                distanceFin,
                timeInSeconds,
                bmp,
                points
            )

            viewModel.addJourney(journey)

            stopJourney()

        }
    }

    private fun stopJourney() {
        sendCommandToService(ACTION_STOP_SERVICE)
    }







    private fun sendCommandToService(action: String) =
        requireActivity().startService(Intent(context, GpsTrackerService::class.java).also {
            it.action = action
            requireActivity().startService(it)
        })

    // Following are the functions to handle the lifecycle of our map. Removing these functions may cause the app to crash.
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
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