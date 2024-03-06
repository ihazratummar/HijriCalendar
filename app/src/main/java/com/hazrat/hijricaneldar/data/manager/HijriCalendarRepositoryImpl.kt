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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn

class HijriCalendarRepositoryImpl(
    private val api: HijriCalendarApi,
    private val gregorianToHijriDao: GregorianToHijriDao,
    private val hijriCalendarDao: HijriCalendarDao
) : HijriCalendarRepository {
    override suspend fun getHijriCalendarFromApi(): HijriCalendarResponse {
        val dataList: List<GregorianToHijriEntity> = gregorianToHijriEntity().firstOrNull()
            ?: throw IllegalStateException("GregorianToHijriEntity list is null or empty")

        if (dataList.isNotEmpty()) {
            val singleData = dataList.first()
            val month = singleData.monthNumber
            val year = singleData.year

            val apiResponse = api.getHijriCalendar(month, year)
            apiResponse.data.forEach { apiDates ->
                val hijriCalendarEntity = convertApiResponseToEntity(apiDates)
                insertCalendarList(hijriCalendarEntity)
            }
            return apiResponse
        } else {
            // Handle the case when GregorianToHijriEntity list is empty
            throw IllegalStateException("GregorianToHijriEntity list is empty")
        }
    }

    override fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>> =
        gregorianToHijriDao.getGregorianToHijriData().flowOn(Dispatchers.IO)
            .conflate()


    override suspend fun insertCalendarList(hijriCalendarList: HijriCalendarEntity): HijriCalendarEntity {
        hijriCalendarDao.insertHijriCalendar(hijriCalendarList)
        return hijriCalendarList
    }

    override fun getCalendarList(): Flow<List<HijriCalendarEntity>> =
        hijriCalendarDao.getCalendarList().flowOn(Dispatchers.Default)
            .conflate()


    private fun convertApiResponseToEntity(apiResponse: Data): HijriCalendarEntity {
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
            gregorianWeekDayEn = gregorian.weekday.en,
            holidays = hijri.holidays
        )
    }
}