package com.tokopedia.sellerapp.domain.mapper

import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.domain.mapper.SummaryDomainMapper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.domain.mapper.SummaryDomainMapper.DATAKEY_READY_TO_SHIP
import com.tokopedia.sellerapp.domain.model.OrderModel

object OrderDomainMapper {
    val STATUS_NEW_ORDER = arrayOf("0") // Actual value should be 220, but temporary hardcoded to 0 for testing purpose
    val STATUS_READY_TO_SHIP = arrayOf("10") // temporary, to be confirmed later

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

    fun getOrderStatusByDataKey(dataKey: String) : Array<String> {
        return when(dataKey){
            DATAKEY_NEW_ORDER -> STATUS_NEW_ORDER
            DATAKEY_READY_TO_SHIP -> STATUS_READY_TO_SHIP
            else -> emptyArray()
        }
    }
}