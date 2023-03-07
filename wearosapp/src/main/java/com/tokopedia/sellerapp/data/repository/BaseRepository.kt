package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.remote.AcceptBulkOrderModel
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface BaseRepository<T> {

    fun getCachedData(params: Array<String> = arrayOf()) : Flow<T>

    fun getCachedDataCount(params: Array<String> = arrayOf()) : Flow<Int> = flowOf()

    suspend fun sendMessagesToNodes(action: Action) { }

}