package com.example.alarmmaps.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmmaps.data.RepositoryImpl
import com.example.alarmmaps.data.room.DatabaseAlarmList
import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.usecases.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    lateinit var alarmList: LiveData<List<Alarm>>

    private val repository by lazy {
        val daoAlarmList = DatabaseAlarmList.getDatabase(application).daoAlarmList()
        RepositoryImpl(daoAlarmList)
    }


    private val getAlarmListUseCase = GetAlarmListUseCase(repository)
    private val getAlarmUseCase = GetAlarmUseCase(repository)
    private val setAlarmUseCase = SetAlarmUseCase(repository)
    private val editAlarmUseCase = EditAlarmUseCase(repository)
    private val deleteAlarmUseCase = DeleteAlarmUseCase(repository)


    fun setAlarmList() {
        alarmList = getAlarmListUseCase.getAlarmList()
    }


    fun setAlarm(alarm: Alarm) = viewModelScope.launch {
        setAlarmUseCase.setAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm) = viewModelScope.launch {
        deleteAlarmUseCase.deleteAlarm(alarm)
    }

    fun editAlarm(alarm: Alarm) = viewModelScope.launch {
        editAlarmUseCase.editAlarm(alarm)
    }

    fun getAlarm(alarmID: Int) = viewModelScope.launch {
        getAlarmUseCase.getAlarm(alarmID)
    }
}

