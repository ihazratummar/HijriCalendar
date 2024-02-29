package com.hazrat.hijricaneldar.data.manager

import com.hazrat.hijricaneldar.data.dao.GregorianToHijriDao
import com.hazrat.hijricaneldar.data.dao.HijriCalendarDao
import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.data.entity.HijriCalendarEntity
import com.hazrat.hijricaneldar.domain.model.hijricalendar.Data
import com.hazrat.hijricaneldar.domain.model.hijricalendar.HijriCalendarResponse
import com.hazrat.hijricaneldar.domain.repository.HijriCalendarRepository
import com.hazrat.hijricaneldar.network.HijriCalendarApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn

class HijriCalendarRepositoryImpl(
    private val api: HijriCalendarApi,
    private val gregorianToHijriDao: GregorianToHijriDao,
    private val hijriCalendarDao: HijriCalendarDao
) : HijriCalendarRepository {
    override suspend fun getHijriCalendarFromApi(): HijriCalendarResponse {
        val singleData: GregorianToHijriEntity = gregorianToHijriEntity()
        val month  = singleData.monthNumber
        val year  = singleData.year

        val apiResponse = api.getHijriCalendar(month, year)
        apiResponse.data.forEach { apiDates ->
            val hijriCalendarEntity = convertApiResponseToEntity(apiDates)
            insertCalendarList(hijriCalendarEntity)
        }
        return  apiResponse
    }

    override suspend fun gregorianToHijriEntity():GregorianToHijriEntity {
        return gregorianToHijriDao.getGregorianToHijriData()
    }

    override suspend fun insertCalendarList(hijriCalendarList: HijriCalendarEntity): HijriCalendarEntity {
         hijriCalendarDao.insertHijriCalendar(hijriCalendarList)
        return hijriCalendarList
    }

    override fun getCalendarList(): Flow<List<HijriCalendarEntity>>  = hijriCalendarDao.getCalendarList().flowOn(Dispatchers.IO)
        .conflate()


    private fun convertApiResponseToEntity(apiResponse: Data): HijriCalendarEntity{
        val hijri = apiResponse.hijri
        val gregorian = apiResponse.gregorian
        return HijriCalendarEntity(
            hijriDay = hijri.day.toInt(),
            gregorianDay = gregorian.day.toInt(),
            hijriDate = hijri.date,
            gregorianDate = gregorian.date,
            hijriYear = hijri.year,
            hijriAbbreviated = hijri.designation.abbreviated,
            gregorianYear = gregorian.year,
            gregorianAbbreviated = gregorian.designation.abbreviated,
            hijriMonthNumber = hijri.month.number,
            hijriMonthEn = hijri.month.en,
            hijriMonthAr = hijri.month.ar,
            gregorianMonthNumber = gregorian.month.number,
            gregorianMonthEn = gregorian.month.en,
            hijriWeekDayEn = hijri.weekday.en,
            hijriWeekDayAr = hijri.weekday.ar,
            gregorianWeekDayEn = gregorian.weekday.en
        )
    }
}