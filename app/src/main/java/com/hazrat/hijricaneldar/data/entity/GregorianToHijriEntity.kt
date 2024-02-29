package com.hazrat.hijricaneldar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity()
data class GregorianToHijriEntity(
    @PrimaryKey
    val id: Long = 1,

    val date: String,
    val day: Int,
    val weekdayEn: String,
    val weekDayAr: String,
    val monthNumber: Int,
    val monthEn: String,
    val monthAr: String,
    val year: String,
    val designationAabbreviated: String,
    val designationExpanded: String,
    val holidays: String? = "No Events"
)
