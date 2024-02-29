package com.hazrat.hijricaneldar.network

import com.hazrat.hijricaneldar.domain.model.gregoriantohijri.GregorianToHijriResponse
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface GregorianToHijriApi {

    @GET("v1/gToH/{date}")
    suspend fun getGtoHDate(
        @Path("date") month: String
    ): GregorianToHijriResponse

}