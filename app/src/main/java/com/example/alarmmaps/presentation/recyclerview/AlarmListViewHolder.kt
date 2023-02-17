package com.example.alarmmaps.presentation.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmaps.R

class AlarmListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.tv_name)
    val radius: TextView = view.findViewById(R.id.tv_radius)
}