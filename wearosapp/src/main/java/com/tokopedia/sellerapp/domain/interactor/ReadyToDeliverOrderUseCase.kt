package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.OrderRepository
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.mapToDomainModel
import com.tokopedia.sellerapp.presentation.model.TITLE_READY_TO_DELIVER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadyToDeliverOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) : OrderUseCase {

    override fun getOrderList(): Flow<List<OrderModel>> {
        return orderRepository.getCachedReadyToDeliverOrderList().map {
            it.mapToDomainModel()
        }
    }

    override fun getCount(): Flow<Pair<String, Int>> {
        return orderRepository.getCachedReadyToDeliverOrderCount().map {
            Pair(TITLE_READY_TO_DELIVER, it)
        }
    }
}