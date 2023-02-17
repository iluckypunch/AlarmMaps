package com.example.alarmmaps.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.alarmmaps.domain.entity.Alarm

@Dao
interface DaoAlarmList {

    @Query("SELECT * FROM AlarmTable")
    fun getAlarmList(): LiveData<List<Alarm>>

    @Query("SELECT * FROM AlarmTable WHERE id = :alarmID")
    fun getAlarm(alarmID: Int): Alarm

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}