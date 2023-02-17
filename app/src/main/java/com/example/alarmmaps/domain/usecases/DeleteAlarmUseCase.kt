package com.example.alarmmaps.domain.usecases

import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class DeleteAlarmUseCase(private val repository: Repository) {

    suspend fun deleteAlarm(alarm: Alarm) {
        repository.deleteAlarm(alarm)
    }
}