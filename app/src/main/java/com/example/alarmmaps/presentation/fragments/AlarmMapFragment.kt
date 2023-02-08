package com.example.alarmmaps.presentation.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alarmmaps.databinding.AlarmMapFragmentBinding
import com.yandex.mapkit.MapKitFactory


class AlarmMapFragment : Fragment() {

    private var _binding: AlarmMapFragmentBinding? = null
    private val binding: AlarmMapFragmentBinding
        get() = _binding ?: throw Exception("AlarmMapFragmentBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlarmMapFragmentBinding.inflate(inflater, container, false)
        MapKitFactory.initialize(this.context)
        return binding.root
    }
}