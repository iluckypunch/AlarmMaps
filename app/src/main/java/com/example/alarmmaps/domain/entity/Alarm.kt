package com.example.alarmmaps.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Alarm(
    val id: Int,
    val longitude: Float,
    val latitude: Float,
    val radius: Float
): Parcelable {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
