package com.example.alarmmaps.presentation.fragments


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.alarmmaps.R
import com.example.alarmmaps.databinding.AlarmMapFragmentBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.logo.Alignment
import com.yandex.mapkit.logo.HorizontalAlignment
import com.yandex.mapkit.logo.VerticalAlignment
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView


class AlarmMapFragment : Fragment(), UserLocationObjectListener, CameraListener {

    private var _binding: AlarmMapFragmentBinding? = null
    private val binding: AlarmMapFragmentBinding
        get() = _binding ?: throw Exception("AlarmMapFragmentBinding = null")

    private lateinit var mapView: MapView

    private lateinit var checkLocationPermission: ActivityResultLauncher<Array<String>>

    private lateinit var userLocationLayer: UserLocationLayer

    private var routeStartLocation = Point(0.0, 0.0)

    private var permissionLocation = false
    private var followUserLocation = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AlarmMapFragment", "OnCreateView")
        _binding = AlarmMapFragmentBinding.inflate(inflater, container, false)
        MapKitFactory.initialize(this.context)
        mapView = binding.mapview
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[ACCESS_FINE_LOCATION] == true ||
                permissions[ACCESS_COARSE_LOCATION] == true
            ) {
                onMapReady()
            }
        }

        checkPermission()

        userInterface()
    }

    private fun userInterface() {
        val mapLogoAlignment = Alignment(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
        mapView.map.logo.setAlignment(mapLogoAlignment)

        binding.userLocationFab.setOnClickListener {
            if (permissionLocation) {
                cameraUserPosition()

                followUserLocation = true
            } else {
                checkPermission()
            }
        }
    }

    private fun onMapReady() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        cameraUserPosition()

        permissionLocation = true
    }

    private fun cameraUserPosition() {
        if (userLocationLayer.cameraPosition() != null) {
            routeStartLocation = userLocationLayer.cameraPosition()!!.target
            mapView.map.move(
                CameraPosition(routeStartLocation, 16f, 0f, 0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        } else {
            mapView.map.move(CameraPosition(routeStartLocation, 16f, 0f, 0f))
        }
    }

    private fun checkPermission() {
        if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
            checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
        ) {
            onMapReady()
        } else {
            checkLocationPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
    override fun onObjectAdded(p0: UserLocationView) {}

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finish: Boolean
    ) {
        if (finish and followUserLocation) {
            setAnchor()
        } else {
            noAnchor()
        }
    }

    private fun setAnchor() {
        userLocationLayer.setAnchor(
            PointF(
                (mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()
            ),
            PointF(
                (mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat()
            )
        )

        binding.userLocationFab.setImageResource(R.drawable.ic_my_location_black_24dp)

        followUserLocation = false
    }

    private fun noAnchor() {
        userLocationLayer.resetAnchor()

        binding.userLocationFab.setImageResource(R.drawable.ic_location_searching_black_24dp)
    }


}
