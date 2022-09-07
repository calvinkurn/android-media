package com.tokopedia.sellerapp.data.repository

interface WearCacheAction {
    fun saveOrderListToCache(message: String)
    fun saveOrderSummaryToCache(message: String)
}