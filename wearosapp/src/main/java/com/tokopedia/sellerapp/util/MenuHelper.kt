package com.tokopedia.sellerapp.util

import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper
import com.tokopedia.sellerapp.domain.mapper.SummaryDomainMapper
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import com.tokopedia.sellerapp.presentation.model.TITLE_READY_TO_SHIP

object MenuHelper {
    const val DATAKEY_UNREAD_CHAT = "unreadChat"
    const val DATAKEY_NEW_ORDER = "newOrder"
    const val DATAKEY_READY_TO_SHIP = "readyToShipOrder"

    fun List<OrderModel>.getDataKeyByOrderStatus() : String {
        return when(this){
            OrderDomainMapper.STATUS_NEW_ORDER -> DATAKEY_NEW_ORDER
            OrderDomainMapper.STATUS_READY_TO_SHIP -> DATAKEY_READY_TO_SHIP
            else -> ""
        }
    }

    fun getTitleByDataKey(dataKey: String) : String {
        return when(dataKey) {
            DATAKEY_NEW_ORDER -> TITLE_NEW_ORDER
            DATAKEY_READY_TO_SHIP -> TITLE_READY_TO_SHIP
            else -> ""
        }
    }
}