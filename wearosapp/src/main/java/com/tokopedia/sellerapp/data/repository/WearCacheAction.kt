package com.tokopedia.sellerapp.data.repository

import android.util.Log

interface WearCacheAction {
    fun saveOrderListToCache(message: String)
    fun saveOrderSummaryToCache(message: String)
}