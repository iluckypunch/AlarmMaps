package com.example.alarmmaps.domain.usecases

import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class SetAlarmUseCase(private val repository: Repository) {

    suspend fun setAlarm(alarm: Alarm) {
        repository.setAlarm(alarm)
    }
}