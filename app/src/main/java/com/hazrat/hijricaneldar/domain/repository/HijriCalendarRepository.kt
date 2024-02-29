package com.hazrat.hijricaneldar.domain.repository

import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import com.hazrat.hijricaneldar.domain.model.hijricalendar.HijriCalendarResponse
import kotlinx.coroutines.flow.Flow

interface HijriCalendarRepository {

    suspend fun getHijriCalendarFromApi(): HijriCalendarResponse?

    suspend fun gregorianToHijriEntity(): GregorianToHijriEntity

    suspend fun insertCalendarList(hijriCalendarList: HijriCalendarEntity): HijriCalendarEntity

    fun getCalendarList(): Flow<List<HijriCalendarEntity>>

}