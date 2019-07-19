package com.tokopedia.transaction.orders

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkOMSConstant {

    @JvmField
    val HOST_EVENT = "events/"

    @JvmField
    val HOST_DEALS = "deals/"

    @JvmField
    val INTERNAL_EVENTS = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_EVENT}"

    @JvmField
    val INTERNAL_DEALS = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_DEALS}"
}