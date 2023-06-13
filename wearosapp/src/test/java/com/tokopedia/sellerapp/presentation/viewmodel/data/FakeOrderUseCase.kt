package com.tokopedia.sellerapp.presentation.viewmodel.data

import com.tokopedia.sellerapp.domain.interactor.OrderUseCase
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.presentation.viewmodel.util.DummyData.listOrderData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

class FakeOrderUseCase: OrderUseCase {
    override fun getOrderList(dataKey: String): Flow<List<OrderModel>> = flow {
        val arrayStatus = OrderDomainMapper.getOrderStatusByDataKey(dataKey)
        emit(listOrderData.filter { it.orderStatusId in arrayStatus })
    }

    override fun getOrderDetail(orderId: String): Flow<OrderModel> = flow {
        emit(listOrderData.first { it.orderId == orderId })
    }

    override fun getCount(dataKey: String): Flow<Pair<String, Int>> = emptyFlow()
}