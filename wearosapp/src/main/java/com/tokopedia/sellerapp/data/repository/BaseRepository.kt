package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface BaseRepository<T> {

    fun getCachedData() : Flow<T>

    fun getCachedDataCount() : Flow<Int> = flowOf()

    suspend fun sendMessagesToNodes(action: Action) { }

}