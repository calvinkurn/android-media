package com.tokopedia.sellerapp.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSummary(list: List<SummaryEntity>)

    @Transaction
    @Query("SELECT * FROM WearSummary")
    fun getSummaryList() : Flow<List<SummaryEntity>>

    @Query("DELETE FROM WearSummary")
    fun clear()
}