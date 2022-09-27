package com.tokopedia.sellerapp.domain.mapper

import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.domain.model.OrderModel

object OrderDomainMapper {
    fun List<OrderWithProduct>.mapToDomainModel() : List<OrderModel> {
        return map { obj ->
            OrderModel(
                orderId = obj.order.orderId,
                orderStatusId = obj.order.orderStatusId,
                orderTotalPrice = obj.order.orderTotalPrice,
                orderDate = obj.order.orderDate,
                deadLineText = obj.order.deadLineText,
                courierName = obj.order.courierName,
                destinationProvince = obj.order.destinationProvince,
                products = obj.products.map { OrderModel.Product(productName = it.productName, orderNote = it.orderNote) }
            )
        }
    }
}