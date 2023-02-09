package com.example.alarmmaps.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.alarmmaps.domain.entity.Alarm

@Dao
interface DaoAlarmList {

    @Query("SELECT * FROM AlarmTable")
    fun getAlarmList(): LiveData<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}