package com.tokopedia.sellerapp.data.datasource.local.dao

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Query
import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSummary(list: List<SummaryEntity>)

    @Transaction
    @Query("SELECT * FROM WearSummary")
    fun getSummaryList() : Flow<List<SummaryEntity>>
}