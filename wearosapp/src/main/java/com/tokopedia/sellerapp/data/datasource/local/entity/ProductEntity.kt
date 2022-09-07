package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "WearOrderProduct")
data class ProductEntity(
    @PrimaryKey @ColumnInfo(name = "order_id")
    var orderId: String = "",

    @Nullable @ColumnInfo(name = "product_name")
    val productName: String = "",

    @Nullable @ColumnInfo(name = "order_note")
    val orderNote: String = "",
)