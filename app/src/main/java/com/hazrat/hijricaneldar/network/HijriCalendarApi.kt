package com.hazrat.hijricaneldar.network

import com.hazrat.hijricaneldar.domain.model.hijricalendar.HijriCalendarResponse
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton


@Singleton
interface HijriCalendarApi {

    @GET("v1/hToGCalendar/{month}/{year}")
    suspend fun getHijriCalendar(
        @Path("month")month: Int,
        @Path("year")year: String
    ): HijriCalendarResponse

}