package com.tokopedia.tkpdpdp.courier

interface CourierTypeFactory {

    fun type(data: CourierViewData): Int

}
