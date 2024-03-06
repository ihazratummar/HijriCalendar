package com.hazrat.hijricaneldar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.hijricaneldar.data.dao.GregorianToHijriDao
import com.hazrat.hijricaneldar.data.dao.HijriCalendarDao
import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity

@Database(entities = [GregorianToHijriEntity::class, HijriCalendarEntity::class], version = 5)
abstract class AppDatabase: RoomDatabase() {
    abstract fun gregorianToHijriDao(): GregorianToHijriDao

    abstract fun hijriCalendarDao() : HijriCalendarDao
}