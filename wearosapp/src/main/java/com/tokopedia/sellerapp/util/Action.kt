package com.tokopedia.sellerapp.util

import com.tokopedia.sellerapp.util.MessageConstant.ACCEPT_BULK_ORDER_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_NOTIFICATION_LIST_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_ORDER_LIST_PATH

enum class Action {
    GET_ORDER_LIST,
    GET_NOTIFICATION_LIST,
    ACCEPT_BULK_ORDER;

    fun getPath() = when(this) {
        GET_ORDER_LIST -> GET_ORDER_LIST_PATH
        GET_NOTIFICATION_LIST -> GET_NOTIFICATION_LIST_PATH
        ACCEPT_BULK_ORDER -> ACCEPT_BULK_ORDER_PATH
    }
}