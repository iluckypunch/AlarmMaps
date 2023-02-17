package com.example.alarmmaps.domain.usecases

import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class EditAlarmUseCase(private val repository: Repository) {

    suspend fun editAlarm(alarm: Alarm) {
        repository.editAlarm(alarm)
    }
}