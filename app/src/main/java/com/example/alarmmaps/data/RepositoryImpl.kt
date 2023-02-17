package com.example.alarmmaps.data

import androidx.lifecycle.LiveData
import com.example.alarmmaps.data.room.RepositoryAlarmList
import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class RepositoryImpl(private val repositoryAlarmList: RepositoryAlarmList) : Repository {

    private var autoIncrementID = 0
    override suspend fun setAlarm(alarm: Alarm) {
        repositoryAlarmList.insert(alarm)
    }

    override fun getAlarmList(): LiveData<List<Alarm>> {
        return repositoryAlarmList.alarmList
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        repositoryAlarmList.delete(alarm)
    }

    override suspend fun editAlarm(alarm: Alarm) {
        repositoryAlarmList.insert(alarm)
    }

    override fun getAlarm(alarmID: Int): Alarm {
        return repositoryAlarmList.getAlarm(alarmID)
    }
}