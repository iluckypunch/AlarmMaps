package com.example.alarmmaps.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alarmmaps.domain.entity.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class DatabaseAlarmList: RoomDatabase() {

    abstract fun daoAlarmList(): DaoAlarmList

    companion object {
        @Volatile
        private var INSTANCE: DatabaseAlarmList? = null

        fun getDatabase(
            context: Context
        ): DatabaseAlarmList {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseAlarmList::class.java,
                    "BinInfoTable"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}