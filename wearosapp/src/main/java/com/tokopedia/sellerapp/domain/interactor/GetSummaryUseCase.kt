package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.domain.model.SummaryModel
import kotlinx.coroutines.flow.Flow

interface GetSummaryUseCase {
    fun getMenuItemCounter(): Flow<List<SummaryModel>>
    fun getOrderSummary(dataKey: String): Flow<SummaryModel>
    suspend fun fetchOrderSummaryData()
}