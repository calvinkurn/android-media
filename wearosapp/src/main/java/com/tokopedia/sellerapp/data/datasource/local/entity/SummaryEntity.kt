package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WearSummary")
data class SummaryEntity(
    @PrimaryKey @ColumnInfo(name = "dataKey")
    var dataKey: String = "",
    
    @ColumnInfo(name = "value")
    var value: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",
)