package com.example.alarmmaps.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "AlarmTable")
data class Alarm(
    @PrimaryKey val id: Int,
    val name: String,
    val longitude: Float,
    val latitude: Float,
    val radius: Float
): Parcelable {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
