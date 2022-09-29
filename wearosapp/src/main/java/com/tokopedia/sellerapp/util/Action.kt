package com.tokopedia.sellerapp.util

import com.tokopedia.sellerapp.util.MessageConstant.ACCEPT_BULK_ORDER_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_ORDER_LIST_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_SUMMARY_PATH

enum class Action {
    GET_ORDER_LIST,
    ACCEPT_BULK_ORDER,
    GET_SUMMARY;

    fun getPath() = when(this) {
        GET_ORDER_LIST -> GET_ORDER_LIST_PATH
        ACCEPT_BULK_ORDER -> ACCEPT_BULK_ORDER_PATH
        GET_SUMMARY -> GET_SUMMARY_PATH
    }
}