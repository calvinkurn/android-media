package com.tokopedia.sellerapp.presentation.viewmodel.data

import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCase
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.viewmodel.util.DummyData.listSummaryData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSummaryUseCase : GetSummaryUseCase  {
    override fun getMenuItemCounter(): Flow<List<SummaryModel>> = flow {
        emit(listSummaryData)
    }

    override fun getOrderSummary(dataKey: String): Flow<SummaryModel> = flow {
        emit(listSummaryData.first { it.dataKey == dataKey })
    }

    override suspend fun fetchOrderSummaryData() { /* nothing to do */ }
}