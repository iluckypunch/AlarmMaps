package com.example.alarmmaps.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "AlarmTable")
data class Alarm(
    val name: String,
    val longitude: Float,
    val latitude: Float,
    val radius: Float,
    val enable: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
): Parcelable
