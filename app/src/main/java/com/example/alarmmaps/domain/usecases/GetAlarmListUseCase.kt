package com.example.alarmmaps.domain.usecases

import androidx.lifecycle.LiveData
import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class GetAlarmListUseCase(private val repository: Repository) {

    fun getAlarmList(): LiveData<List<Alarm>> {
        return repository.getAlarmList()
    }
}