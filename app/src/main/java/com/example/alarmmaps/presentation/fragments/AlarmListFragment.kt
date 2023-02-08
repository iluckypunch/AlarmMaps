package com.example.alarmmaps.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alarmmaps.databinding.AlarmListFragmentBinding

class AlarmListFragment: Fragment() {

    private var _binding: AlarmListFragmentBinding? = null
    private val binding: AlarmListFragmentBinding
        get() = _binding ?: throw Exception("AlarmListFragmentBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AlarmListFragment", "OnCreateView")
        _binding = AlarmListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}