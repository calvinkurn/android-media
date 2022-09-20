package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.ReadyToShipOrderRepository
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.mapToDomainModel
import com.tokopedia.sellerapp.presentation.model.TITLE_READY_TO_SHIP
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadyToShipOrderUseCase @Inject constructor(
    private val readyToShipOrderRepository: ReadyToShipOrderRepository
) : OrderUseCase {

    override fun getOrderList(): Flow<List<OrderModel>> {
        return readyToShipOrderRepository.getCachedData().map {
            it.mapToDomainModel()
        }
    }

    override fun getCount(): Flow<Pair<String, Int>> {
        return readyToShipOrderRepository.getCachedDataCount().map {
            Pair(TITLE_READY_TO_SHIP, it)
        }
    }
}