package com.hazrat.hijricaneldar.data.manager

import android.util.Log
import com.hazrat.hijricaneldar.Exceptions
import com.hazrat.hijricaneldar.data.dao.GregorianToHijriDao
import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.domain.model.gregoriantohijri.GregorianToHijriResponse
import com.hazrat.hijricaneldar.domain.repository.GregorianToHijriRepository
import com.hazrat.hijricaneldar.network.GregorianToHijriApi
import com.hazrat.hijricaneldar.util.DateUtil
import kotlinx.coroutines.flow.Flow

class GregorianToHijriRepositoryImpl(
    private val api: GregorianToHijriApi,
    private val gregorianToHijriDao: GregorianToHijriDao
): GregorianToHijriRepository {
    override suspend fun getGregorianToHijriDate(): GregorianToHijriResponse {
        val date = DateUtil.getCurrentDate()
        try {
            val response = api.getGtoHDate(date)
            if (response.code == 200 && response.status == "OK") {
                Log.d("Repository", "Hijri date response: $response")
                saveResponseToDatabase(response)
                return response
            } else {
                // Handle error cases appropriately
                Log.e("Repository", "Failed to get Hijri date: ${response.status}")
                throw Exceptions("Failed to get Hijri date: ${response.status}")
            }
        }catch (e: Exception){
            Log.e("Repository", "Exception occurred: ${e.message}")
            throw e
        }
    }

    override  fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>> {
        return  gregorianToHijriDao.getGregorianToHijriData()
    }

    private suspend fun saveResponseToDatabase(response: GregorianToHijriResponse){
        val data = response.data.hijri
        val hijriEntity = GregorianToHijriEntity(
            date = data.date,
            day = data.day.toInt(),
            weekdayEn = data.weekday.en,
            weekDayAr = data.weekday.ar,
            monthNumber = data.month.number,
            monthAr = data.month.ar,
            monthEn = data.month.en,
            year = data.year,
            designationAbbreviated = data.designation.abbreviated,
            designationExpanded = data.designation.expanded,
            holidays = data.holidays.joinToString(" ")?: "No Events"
        )
        gregorianToHijriDao.insertOrUpdate(hijriEntity)
    }
}


