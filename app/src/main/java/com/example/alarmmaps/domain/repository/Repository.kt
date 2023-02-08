package com.example.alarmmaps.domain.repository

import com.example.alarmmaps.domain.entity.Alarm

interface Repository {

    fun setAlarm(longitude: Float, latitude: Float, radius: Float): Alarm

    fun getAlarmList(): List<Alarm>
}