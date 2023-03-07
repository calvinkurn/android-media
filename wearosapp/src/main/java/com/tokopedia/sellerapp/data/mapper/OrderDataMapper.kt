package com.tokopedia.sellerapp.data.mapper

import com.google.gson.Gson
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity
import com.tokopedia.sellerapp.data.datasource.remote.OrderListModel

object OrderDataMapper {
    fun mapMessageDataToModel(message: String) : OrderListModel {
        return Gson().fromJson(message, OrderListModel::class.java)
    }

    fun OrderListModel.mapModelToOrderEntity() : List<OrderEntity> {
        return orderList.list.map {
            OrderEntity(
                orderId = it.orderId,
                orderStatusId = it.orderStatusId,
                status = it.status,
                orderTotalPrice = it.orderTotalPrice,
                orderDate = it.orderDate,
                deadLineText = it.deadLineText,
                courierName = it.courierName,
                courierType = it.courierType,
                destinationProvince = it.destinationProvince,
            )
        }
    }

    fun OrderListModel.mapModelToProductEntity() : List<ProductEntity> {
        val list = mutableListOf<ProductEntity>()
        orderList.list.forEach { order ->
            order.products.forEach { prod ->
                list.add(
                    ProductEntity(
                        productId = prod.productId,
                        orderId = order.orderId,
                        productName = prod.productName,
                        productQty = prod.productQty,
                        picture = prod.picture,
                        orderNote = prod.orderNote
                    )
                )
            }
        }
        return list
    }
}