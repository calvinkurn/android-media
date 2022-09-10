package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "WearOrderProduct",
    primaryKeys = ["product_id", "order_id"],
    foreignKeys = [ForeignKey(
        entity = OrderEntity::class,
        parentColumns = ["order_id"],
        childColumns = ["order_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductEntity(
    @ColumnInfo(name = "order_id")
    var orderId: String = "",

    @ColumnInfo(name = "product_id")
    var productId: String = "",

    @ColumnInfo(name = "product_name")
    val productName: String = "",

    @ColumnInfo(name = "product_qty")
    val productQty: Int = 0,

    @ColumnInfo(name = "picture")
    val picture: String = "",

    @ColumnInfo(name = "order_note")
    val orderNote: String = "",
)