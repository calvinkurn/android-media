package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.OrderRepository
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.mapToDomainModel
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadyToDeliverOrderUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : OrderUseCase {

    override fun invoke(): Flow<List<OrderModel>> {
        return orderRepository.getCachedReadyToDeliverOrderList().map { it.mapToDomainModel() }
    }
}