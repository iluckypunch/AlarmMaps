package com.example.alarmmaps.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.alarmmaps.data.room.DaoAlarmList
import com.example.alarmmaps.data.room.DatabaseAlarmList
import com.example.alarmmaps.data.room.RepositoryAlarmList
import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.domain.repository.Repository

object RepositoryImpl: Repository {

    override fun setAlarm(longitude: Float, latitude: Float, radius: Float): Alarm {
        TODO("Not yet implemented")
    }

    override suspend fun getAlarmList(context: Context): LiveData<List<Alarm>> {
        val daoAlarmList = DatabaseAlarmList.getDatabase(context).daoAlarmList()
        val repositoryAlarmList = RepositoryAlarmList(daoAlarmList)
        return repositoryAlarmList.alarmList
    }
}