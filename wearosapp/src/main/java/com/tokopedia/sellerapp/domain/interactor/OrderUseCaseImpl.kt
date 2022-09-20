package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.OrderRepository
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.mapToDomainModel
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : OrderUseCase {

    override fun getOrderList(dataKey: String): Flow<List<OrderModel>> {
        return orderRepository.getCachedData(
            OrderDomainMapper.getOrderStatusByDataKey(dataKey)
        ).map {
            it.mapToDomainModel()
        }
    }

    override fun getCount(dataKey: String): Flow<Pair<String, Int>> {
        return orderRepository.getCachedDataCount(
            OrderDomainMapper.getOrderStatusByDataKey(dataKey)
        ).map {
            Pair(TITLE_NEW_ORDER, it)
        }
    }
}