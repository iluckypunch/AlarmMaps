package com.example.alarmmaps.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmaps.databinding.AlarmListFragmentBinding
import com.example.alarmmaps.presentation.MainViewModel
import com.example.alarmmaps.presentation.recyclerview.AlarmListAdapter

class AlarmListFragment: Fragment() {

    val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var alarmListAdapter: AlarmListAdapter

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
        viewModel.setAlarmList()
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.alarmList.observe(requireActivity()){
            alarmListAdapter.setAlarmList(it)
        }
    }

    private fun setupRecyclerView() {
        val alarmList = binding.rvAlarmList
        with(alarmList) {
                alarmListAdapter = AlarmListAdapter()
                adapter = alarmListAdapter
                layoutManager = LinearLayoutManager(requireActivity())
        }
        setupSwipeListener(alarmList)
    }

    private fun setupSwipeListener(rvAlarmList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val item = alarmListAdapter.alarmList[position]
                viewModel.deleteAlarm(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvAlarmList)
    }
}