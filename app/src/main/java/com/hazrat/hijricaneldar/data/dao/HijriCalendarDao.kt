package com.hazrat.hijricaneldar.data.dao

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HijriCalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHijriCalendar(hijriCalendarEntity: HijriCalendarEntity)


    @Query("SELECT * FROM hijricalendarentity")
    fun getCalendarList(): Flow<List<HijriCalendarEntity>>

    @Query("SELECT * FROM hijricalendarentity WHERE gregorianDate = :gregorianDate")
    fun getHijriCalendarForDate(gregorianDate: String): Flow<List<HijriCalendarEntity>>

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHijriCalendarForDateAsFlow(gregorianDate: LocalDate): Flow<List<HijriCalendarEntity>> {
        val dateString = gregorianDate.toString()
        return getHijriCalendarForDate(dateString)
    }

}