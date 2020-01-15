package com.tokopedia.transaction.orders.orderlist.data.ticker

import com.google.gson.annotations.SerializedName

data class TickerResponse(

        @field:SerializedName("orderTickers")
        val orderTickers: OrderTickers? = null
)