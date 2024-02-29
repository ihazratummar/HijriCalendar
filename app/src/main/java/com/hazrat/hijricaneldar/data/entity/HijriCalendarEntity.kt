package com.hazrat.hijricaneldar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class HijriCalendarEntity(
    @PrimaryKey
    val hijriDay: Int,
    val gregorianDay: Int,
    val hijriDate: String,
    val gregorianDate: String,
    val hijriYear: String,
    val hijriAbbreviated: String,
    val gregorianYear: String,
    val gregorianAbbreviated: String,
    val hijriMonthNumber: Int,
    val hijriMonthEn: String,
    val hijriMonthAr: String,
    val gregorianMonthNumber: Int,
    val gregorianMonthEn: String,
    val hijriWeekDayEn: String,
    val hijriWeekDayAr: String,
    val gregorianWeekDayEn: String
)
