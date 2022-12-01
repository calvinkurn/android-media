package com.tokopedia.sellerapp.util

import com.tokopedia.sellerapp.util.MessageConstant.ACCEPT_BULK_ORDER_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_ALL_DATA
import com.tokopedia.sellerapp.util.MessageConstant.GET_ORDER_LIST_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_PHONE_STATE
import com.tokopedia.sellerapp.util.MessageConstant.GET_SUMMARY_PATH

enum class Action {
    GET_ORDER_LIST,
    ACCEPT_BULK_ORDER,
    GET_ALL_DATA,
    GET_SUMMARY,
    GET_PHONE_STATE,
    OPEN_NEW_ORDER_LIST,
    OPEN_READY_TO_SHIP,
    OPEN_LOGIN_PAGE;

    fun getPath() = when(this) {
        GET_ORDER_LIST -> GET_ORDER_LIST_PATH
        ACCEPT_BULK_ORDER -> ACCEPT_BULK_ORDER_PATH
        GET_SUMMARY -> GET_SUMMARY_PATH
        GET_ALL_DATA -> MessageConstant.GET_ALL_DATA
        GET_PHONE_STATE -> MessageConstant.GET_PHONE_STATE
        OPEN_LOGIN_PAGE -> MessageConstant.OPEN_LOGIN_PAGE
        OPEN_READY_TO_SHIP -> MessageConstant.OPEN_READY_TO_SHIP
        OPEN_NEW_ORDER_LIST -> MessageConstant.OPEN_NEW_ORDER_LIST
    }
}
