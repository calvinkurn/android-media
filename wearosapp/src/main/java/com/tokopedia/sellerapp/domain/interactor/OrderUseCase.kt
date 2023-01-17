package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.datasource.remote.AcceptBulkOrderModel
import com.tokopedia.sellerapp.domain.model.OrderModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface OrderUseCase {
    fun getOrderList(dataKey: String) : Flow<List<OrderModel>>
    fun getOrderDetail(orderId: String) : Flow<OrderModel>
    fun getCount(dataKey: String) : Flow<Pair<String, Int>>
    fun getAcceptBulkOrder(): Flow<AcceptBulkOrderModel> = emptyFlow()
    suspend fun sendRequest() { }
}