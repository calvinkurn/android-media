package com.tokopedia.sellerapp.data.datasource.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity

data class OrderModel(
    @Embedded
    var order: OrderEntity? = null,

    @Relation(
        parentColumn = "order_id",
        entityColumn = "order_id",
        entity = ProductEntity::class
    )
    var product: ProductEntity? = null
)