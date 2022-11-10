package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.domain.model.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderUseCase {
    fun getOrderList(dataKey: String) : Flow<List<OrderModel>>
    fun getOrderDetail(orderId: String) : Flow<OrderModel>
    fun getCount(dataKey: String) : Flow<Pair<String, Int>>
    suspend fun sendRequest() { }
}