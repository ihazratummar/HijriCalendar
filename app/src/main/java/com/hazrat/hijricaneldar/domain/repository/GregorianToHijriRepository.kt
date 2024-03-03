package com.hazrat.hijricaneldar.domain.repository

import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import com.hazrat.hijricaneldar.domain.model.gregoriantohijri.GregorianToHijriResponse
import kotlinx.coroutines.flow.Flow

interface GregorianToHijriRepository {
    suspend fun getGregorianToHijriDate(): GregorianToHijriResponse

    fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>>
}