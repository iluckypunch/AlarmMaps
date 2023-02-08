package com.example.alarmmaps.domain.usecases

import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class SetAlarmUseCase(private val repository: Repository) {

    fun setAlarm(longitude: Float, latitude: Float, radius: Float): Alarm {
        return repository.setAlarm(longitude, latitude, radius)
    }
}