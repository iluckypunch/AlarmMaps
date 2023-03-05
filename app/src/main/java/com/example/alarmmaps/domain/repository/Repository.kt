package com.example.alarmmaps.domain.repository

import androidx.lifecycle.LiveData
import com.example.alarmmaps.domain.entity.Alarm

interface Repository {

    fun getAlarmList(): LiveData<List<Alarm>>
    suspend fun setAlarm(alarm: Alarm)
    suspend fun editAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun getAlarm(alarmID: Int): Alarm
}