package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.domain.model.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderUseCase {
    fun getOrderList() : Flow<List<OrderModel>>
    fun getCount() : Flow<Pair<String, Int>>
    suspend fun sendRequest() { }
}