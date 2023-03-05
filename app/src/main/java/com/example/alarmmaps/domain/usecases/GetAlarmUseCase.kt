package com.example.alarmmaps.domain.usecases

import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class GetAlarmUseCase(private val repository: Repository) {

    suspend fun getAlarm(alarmID: Int): Alarm {
        return repository.getAlarm(alarmID)
    }
}