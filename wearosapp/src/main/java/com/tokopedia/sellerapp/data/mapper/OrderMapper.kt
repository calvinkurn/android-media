package com.tokopedia.sellerapp.data.mapper

import com.google.gson.Gson
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity
import com.tokopedia.sellerapp.data.datasource.remote.OrderListModel

object OrderMapper {
    fun String.mapMessageDataToModel() : OrderListModel {
        return Gson().fromJson(this, OrderListModel::class.java)
    }

    fun OrderListModel.mapModelToOrderEntity() : List<OrderEntity> {
        return orderList.list.map {
            OrderEntity(
                orderId = it.orderId,
                orderStatusId = it.orderStatusId,
                orderTotalPrice = it.orderTotalPrice,
                orderDate = it.orderDate,
                deadLineText = it.deadLineText,
                courierName = it.courierName,
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
                        orderId = order.orderId,
                        productName = prod.productName,
                        orderNote = prod.orderNote
                    )
                )
            }
        }
        return list
    }
}