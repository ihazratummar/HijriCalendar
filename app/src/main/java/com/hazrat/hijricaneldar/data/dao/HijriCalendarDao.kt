package com.hazrat.hijricaneldar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HijriCalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHijriCalendar(hijriCalendarEntity: HijriCalendarEntity)


    @Query("SELECT * FROM hijricalendarentity")
    fun getCalendarList(): Flow<List<HijriCalendarEntity>>
}