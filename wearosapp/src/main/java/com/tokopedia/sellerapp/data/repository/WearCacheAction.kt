package com.tokopedia.sellerapp.data.repository

interface WearCacheAction {
    fun saveOrderListToCache(message: String)
    fun saveSummaryToCache(message: String)
}