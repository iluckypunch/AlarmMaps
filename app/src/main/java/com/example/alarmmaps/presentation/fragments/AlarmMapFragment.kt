package com.example.alarmmaps.presentation.fragments


import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alarmmaps.R
import com.example.alarmmaps.databinding.AlarmMapFragmentBinding
import com.example.alarmmaps.presentation.MainViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.logo.Alignment
import com.yandex.mapkit.logo.HorizontalAlignment
import com.yandex.mapkit.logo.VerticalAlignment
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.ui_view.ViewProvider


class AlarmMapFragment : Fragment(), UserLocationObjectListener, CameraListener,
    GeoObjectTapListener, InputListener {

    private var _binding: AlarmMapFragmentBinding? = null
    private val binding: AlarmMapFragmentBinding
        get() = _binding ?: throw Exception("AlarmMapFragmentBinding = null")

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var mapView: MapView

    private lateinit var checkLocationPermission: ActivityResultLauncher<Array<String>>

    private lateinit var userLocationLayer: UserLocationLayer

    lateinit var name: String

    private var routeStartLocation = Point(0.0, 0.0)

    private var permissionLocation = false
    private var followUserLocation = false
    private var mapObjectIsSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AlarmMapFragment", "OnCreateView")
        _binding = AlarmMapFragmentBinding.inflate(inflater, container, false)
        MapKitFactory.initialize(this.context)
        mapView = binding.mapview
        binding.takePlaceButton.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {}

        checkPermission()
        userInterface()
        launchScreenMode()
    }

    private fun launchScreenMode() {
        when (arguments?.getString(SCREEN_MODE)) {
            EDIT_MODE -> launchEditMode()
            ADD_MODE -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        val editableAlarm = arguments?.getInt(ALARM_ID)?.let { viewModel.getAlarm(it) }
        val oldPoint = Point(editableAlarm!!.latitude.toDouble(), editableAlarm.longitude.toDouble())
        mapView.map.mapObjects.addCircle(
            Circle(
                oldPoint,
                editableAlarm.radius
            ),
            Color.DKGRAY,
            2f,
            Color.GRAY)
        mapView.map.move(CameraPosition(oldPoint, 16f, 0f, 0f),
        Animation(Animation.Type.SMOOTH, 1f), null)
        binding.takePlaceButton.setOnClickListener {
            val point = mapView.map.cameraPosition.target
            val dialog = SetAlarmDialog()
            val args = Bundle()
            args.putFloat("latitude", point.latitude.toFloat())
            args.putFloat("longitude", point.longitude.toFloat())
            if (mapObjectIsSelected) {
                args.putString("name", name)
            }
            args.putInt("alarm_id", editableAlarm.id!!.toInt())
            dialog.arguments = args
            dialog.show(childFragmentManager, null)
        }
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

    private fun launchAddMode() {
        binding.takePlaceButton.setOnClickListener {
            val point = mapView.map.cameraPosition.target
            val dialog = SetAlarmDialog()
            val args = Bundle()
            args.putFloat("latitude", point.latitude.toFloat())
            args.putFloat("longitude", point.longitude.toFloat())
            if (mapObjectIsSelected) {
                args.putString("name", name)
            }
            dialog.arguments = args
            dialog.show(childFragmentManager, null)
        }
    }

    private fun onMapReady() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        cameraUserPosition()

        mapView.map.addTapListener(this)
        mapView.map.addInputListener(this)
        mapView.map.addCameraListener(this)

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
            mapView.map.move(CameraPosition(routeStartLocation, 1f, 0f, 0f))
        }
    }

    private fun checkLocationPermissionAPI28() {
        if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
            checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
        ) {
            onMapReady()
        } else {
            checkLocationPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun checkLocationPermissionAPI29() {
        if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
            checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED &&
            checkSelfPermission(requireContext(), ACCESS_BACKGROUND_LOCATION) == PERMISSION_GRANTED
        ) {
            onMapReady()
        } else {
            checkLocationPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun checkBackgroundLocationPermissionAPI30() {
        if (checkSelfPermission(requireContext(), ACCESS_BACKGROUND_LOCATION) == PERMISSION_GRANTED) {
            onMapReady()
        } else {
            checkLocationPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.background_location_permission_title)
                .setMessage(R.string.background_location_permission_message)
                .setPositiveButton(R.string.settings) { _,_ ->
                    // this request will take user to xApplication's Setting page
                    checkLocationPermission.launch(arrayOf(ACCESS_BACKGROUND_LOCATION))
                }
                .setNegativeButton(R.string.cancel) { dialog,_ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            checkLocationPermissionAPI28()
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            checkLocationPermissionAPI29()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkBackgroundLocationPermissionAPI30()
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
        if (finish) {
            if (followUserLocation) {
                setAnchor()
                binding.takePlaceButton.show()
                binding.userLocationFab.show()
            } else {
                noAnchor()
                binding.takePlaceButton.show()
                binding.userLocationFab.show()
                if (binding.mapPin.visibility == View.INVISIBLE) {
                    mapObjectIsSelected = true
                }
            }
        } else {
            noAnchor()
            binding.takePlaceButton.hide()
            binding.userLocationFab.hide()
            if (mapObjectIsSelected) {
                binding.mapPin.visibility = View.VISIBLE
                mapObjectIsSelected = false
                mapView.map.deselectGeoObject()
            }
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

    override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {
        val selectionMetadata = geoObjectTapEvent
            .geoObject
            .metadataContainer
            .getItem(GeoObjectSelectionMetadata::class.java)

        val pointOfSelectionMetadata = geoObjectTapEvent.geoObject.geometry[0].point

        if (selectionMetadata != null) {
            mapView.map.selectGeoObject(selectionMetadata.id, selectionMetadata.layerId)
            mapView.map.move(CameraPosition(pointOfSelectionMetadata!!, mapView.map.cameraPosition.zoom, 0f, 0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null)
            binding.mapPin.visibility = View.INVISIBLE
            name = geoObjectTapEvent.geoObject.name.toString()
        }

        return selectionMetadata != null
    }

    override fun onMapTap(map: Map, point: Point) {
        mapView.map.mapObjects.clear()
        mapView.map.deselectGeoObject()
        binding.mapPin.visibility = View.VISIBLE
        mapObjectIsSelected = false
    }

    override fun onMapLongTap(map: Map, point: Point) {
        val view = View(requireContext()).apply {
            background = getDrawable(requireContext(),R.drawable.ic_baseline_location_on_24)
        }
        mapView.map.mapObjects.clear()
        mapView.map.mapObjects.addPlacemark(point, ViewProvider(view))
        mapView.map.move(CameraPosition(point, 16f, 0f, 0f),
        Animation(Animation.Type.SMOOTH, 1f),
        null)
        binding.mapPin.visibility = View.VISIBLE
        mapObjectIsSelected = false
        Log.d("AlarmMapFragment", "${point.latitude}, ${point.longitude}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val ALARM_ID = "extra_alarm_id"
        private const val ADD_MODE = "add_mode"
        private const val EDIT_MODE = "edit_mode"

        fun newInstanceAddAlarm(): AlarmMapFragment {
            return AlarmMapFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_MODE)
                }
            }
        }


        fun newInstanceEditAlarm(alarmID: Int): AlarmMapFragment {
            return AlarmMapFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, EDIT_MODE)
                    putInt(ALARM_ID, alarmID)
                }
            }
        }
    }
}
