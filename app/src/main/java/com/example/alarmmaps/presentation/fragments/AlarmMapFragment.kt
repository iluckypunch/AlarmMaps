package com.example.alarmmaps.presentation.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alarmmaps.databinding.AlarmMapFragmentBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class AlarmMapFragment : Fragment() {

    private var _binding: AlarmMapFragmentBinding? = null
    private val binding: AlarmMapFragmentBinding
        get() = _binding ?: throw Exception("AlarmMapFragmentBinding = null")

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AlarmMapFragment", "OnCreateView")
        _binding = AlarmMapFragmentBinding.inflate(inflater, container, false)
        MapKitFactory.initialize(this.context)
        mapView = binding.mapview
        val cameraPosition = CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f)
        val animation = Animation(Animation.Type.SMOOTH, 5f)
        mapView.map.move(cameraPosition, animation, null)
        return binding.root
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
companion object {
    val TARGET_LOCATION = Point(59.945933, 30.320045)
}

}
