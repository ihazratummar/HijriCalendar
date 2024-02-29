package com.hazrat.hijricaneldar.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.hazrat.hijricaneldar.data.dao.GregorianToHijriDao
import com.hazrat.hijricaneldar.data.dao.HijriCalendarDao
import com.hazrat.hijricaneldar.data.database.AppDatabase
import com.hazrat.hijricaneldar.data.manager.GregorianToHijriRepositoryImpl
import com.hazrat.hijricaneldar.data.manager.HijriCalendarRepositoryImpl
import com.hazrat.hijricaneldar.domain.repository.GregorianToHijriRepository
import com.hazrat.hijricaneldar.domain.repository.HijriCalendarRepository
import com.hazrat.hijricaneldar.network.GregorianToHijriApi
import com.hazrat.hijricaneldar.network.HijriCalendarApi
import com.hazrat.hijricaneldar.util.Constants.GTH_BASE_URL
import com.hazrat.hijricaneldar.util.Constants.HIJRI_CALENDAR_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGtoHManager(api: GregorianToHijriApi,gregorianToHijriDao: GregorianToHijriDao ): GregorianToHijriRepository{
        return GregorianToHijriRepositoryImpl(api,gregorianToHijriDao)
    }

    @Singleton
    @Provides
    fun provideGregorianToHijriApi(): GregorianToHijriApi{
         return Retrofit.Builder()
             .baseUrl(GTH_BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(GregorianToHijriApi::class.java)
    }



    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        Log.d("AppDatabase", "Creating database instance")
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideGregorianToHijriDao(appDatabase: AppDatabase): GregorianToHijriDao{
        return appDatabase.gregorianToHijriDao()
    }

    @Singleton
    @Provides
    fun provideHijriCalenderDao(appDatabase: AppDatabase): HijriCalendarDao{
        return appDatabase.hijriCalendarDao()
    }


    @Singleton
    @Provides
    fun hijriCalendarManager(api: HijriCalendarApi, dao: GregorianToHijriDao, hijriCalendarDao: HijriCalendarDao): HijriCalendarRepository{
        return HijriCalendarRepositoryImpl(api, dao,hijriCalendarDao)
    }

    @Singleton
    @Provides
    fun provideHijriCalendarApi(): HijriCalendarApi{
        return  Retrofit.Builder()
            .baseUrl(HIJRI_CALENDAR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HijriCalendarApi::class.java)
    }
}