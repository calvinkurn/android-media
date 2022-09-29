package com.tokopedia.sellerapp.data.mapper

import com.google.gson.Gson
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity
import com.tokopedia.sellerapp.data.datasource.remote.OrderListModel

object NotificationDataMapper {
    fun String.mapMessageDataToModel() : OrderListModel {
        return Gson().fromJson(this, OrderListModel::class.java)
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
}