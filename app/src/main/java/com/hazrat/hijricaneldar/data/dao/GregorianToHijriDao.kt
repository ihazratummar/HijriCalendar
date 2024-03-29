package com.hazrat.hijricaneldar.data.dao

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.hijricaneldar.data.entity.GregorianToHijriEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GregorianToHijriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: GregorianToHijriEntity)

    @Query("SELECT * FROM gregoriantohijrientity WHERE id = 1")
    fun  getGregorianToHijriData():Flow<List<GregorianToHijriEntity>>
}
