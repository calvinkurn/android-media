package com.tokopedia.sellerapp.data.datasource.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity

data class OrderWithProduct(
    @Embedded
    var order: OrderEntity,

    @Relation(
        parentColumn = "order_id",
        entityColumn = "order_id",
        entity = ProductEntity::class
    )
    var products: List<ProductEntity>
)