package com.example.alarmmaps.data.room

import androidx.lifecycle.LiveData
import com.example.alarmmaps.domain.entity.Alarm

class RepositoryAlarmList(private val daoAlarmList: DaoAlarmList) {

    val alarmList: LiveData<List<Alarm>> = daoAlarmList.getAlarmList()

    suspend fun insert(alarm: Alarm) {
        daoAlarmList.insertAlarm(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        daoAlarmList.deleteAlarm(alarm)
    }
}