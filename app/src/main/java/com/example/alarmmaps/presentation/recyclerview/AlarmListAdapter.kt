package com.example.alarmmaps.presentation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmaps.R
import com.example.alarmmaps.domain.entity.Alarm

class AlarmListAdapter: RecyclerView.Adapter<AlarmListViewHolder>() {

    var alarmList = emptyList<Alarm>()

    var onAlarmLongClickListener: ((Alarm) -> Unit)? = null

    internal fun setAlarmList(alarmList: List<Alarm>) {
        this.alarmList = alarmList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.alarm_enabled_cardview
            VIEW_TYPE_DISABLED -> R.layout.alarm_disabled_cardview
            else -> throw RuntimeException("Unknown viewType $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return AlarmListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onBindViewHolder(holder: AlarmListViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.tvName.text = alarm.name
        holder.tvRadius.text = alarm.radius.toString()
        holder.view.setOnLongClickListener {
            onAlarmLongClickListener?.invoke(alarm)
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val alarm = alarmList[position]
        return if (alarm.enable) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
    }
}