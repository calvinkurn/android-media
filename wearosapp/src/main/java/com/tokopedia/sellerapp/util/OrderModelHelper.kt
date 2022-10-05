package com.tokopedia.sellerapp.util

import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.util.OrderType.NEW_ORDER_TYPE
import com.tokopedia.sellerapp.util.OrderType.READY_TO_SHOP_TYPE

object OrderModelHelper {

    fun List<OrderModel>.getOrderType(): String {
        return if (all { it.orderStatusId in OrderDomainMapper.STATUS_NEW_ORDER }) {
            NEW_ORDER_TYPE
        } else {
            READY_TO_SHOP_TYPE
        }
    }
}