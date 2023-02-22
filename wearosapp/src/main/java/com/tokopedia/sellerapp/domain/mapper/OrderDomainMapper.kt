package com.tokopedia.sellerapp.domain.mapper

import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_READY_TO_SHIP

object OrderDomainMapper {
    val STATUS_NEW_ORDER = arrayOf("220")
    val STATUS_READY_TO_SHIP = arrayOf("400","520")

    fun List<OrderWithProduct>.mapToDomainModel() : List<OrderModel> {
        return map { obj ->
            mapToOrderModel(obj)
        }
    }

    private fun mapToOrderModel(obj: OrderWithProduct): OrderModel {
        return OrderModel(
            orderId = obj.order.orderId,
            orderStatusId = obj.order.orderStatusId,
            orderTotalPrice = obj.order.orderTotalPrice,
            orderDate = obj.order.orderDate,
            deadLineText = obj.order.deadLineText,
            courierName = obj.order.courierName,
            destinationProvince = obj.order.destinationProvince,
            products = obj.products.map { OrderModel.Product(
                productName = it.productName,
                productImage = it.picture,
                orderNote = it.orderNote
            ) }
        )
    }

    fun OrderWithProduct.mapToDomainModel(): OrderModel {
        return mapToOrderModel(this)
    }

    fun getOrderStatusByDataKey(dataKey: String) : Array<String> {
        return when(dataKey){
            DATAKEY_NEW_ORDER -> STATUS_NEW_ORDER
            DATAKEY_READY_TO_SHIP -> STATUS_READY_TO_SHIP
            else -> emptyArray()
        }
    }
}