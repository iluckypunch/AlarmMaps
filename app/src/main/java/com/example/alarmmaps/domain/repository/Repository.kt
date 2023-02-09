package com.example.alarmmaps.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.alarmmaps.domain.entity.Alarm

interface Repository {

    fun setAlarm(longitude: Float, latitude: Float, radius: Float): Alarm

    suspend fun getAlarmList(context: Context): LiveData<List<Alarm>>
}