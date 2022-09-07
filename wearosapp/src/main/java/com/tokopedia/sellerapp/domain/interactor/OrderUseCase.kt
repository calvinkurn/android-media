package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.domain.model.OrderModel
import kotlinx.coroutines.flow.Flow


interface OrderUseCase {
    operator fun invoke() : Flow<List<OrderModel>>
    suspend fun sendRequest()
}