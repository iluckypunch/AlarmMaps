package com.example.alarmmaps.data

import androidx.lifecycle.LiveData
import com.example.alarmmaps.data.room.DaoAlarmList
import com.example.alarmmaps.data.room.RepositoryAlarmList
import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

class RepositoryImpl(daoAlarmList: DaoAlarmList) : Repository {


    private val repositoryAlarmList = RepositoryAlarmList(daoAlarmList)

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