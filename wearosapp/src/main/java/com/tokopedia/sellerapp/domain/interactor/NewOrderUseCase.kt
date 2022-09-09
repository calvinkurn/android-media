package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.NewOrderRepository
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.mapToDomainModel
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewOrderUseCase @Inject constructor(
    private val newOrderRepository: NewOrderRepository
) : OrderUseCase {

    override fun getOrderList(): Flow<List<OrderModel>> {
        return newOrderRepository.getCachedData().map {
            it.mapToDomainModel()
        }
    }

    override fun getCount(): Flow<Pair<String, Int>> {
        return newOrderRepository.getCachedDataCount().map {
            Pair(TITLE_NEW_ORDER, it)
        }
    }
}