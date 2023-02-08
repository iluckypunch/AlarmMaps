package com.example.alarmmaps.domain.usecases

import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class GetAlarmListUseCase(private val repository: Repository) {

    fun getAlarmList(): List<Alarm> {
        return repository.getAlarmList()
    }
}