package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "WearOrder")
data class OrderEntity(
    @PrimaryKey @ColumnInfo(name = "order_id")
    var orderId: String = "",
    
    @ColumnInfo(name = "order_status_id")
    var orderStatusId: String = "",

    @ColumnInfo(name = "status")
    var status: String = "",

    @ColumnInfo(name = "order_total_price")
    var orderTotalPrice: String = "",

    @ColumnInfo(name = "order_date")
    var orderDate: String = "",

    @ColumnInfo(name = "deadline_text")
    var deadLineText: String = "",

    @ColumnInfo(name = "courier_name")
    var courierName: String = "",

    @ColumnInfo(name = "courier_product_name")
    var courierType: String = "",

    @Nullable @ColumnInfo(name = "destination_province")
    var destinationProvince: String = "",
)