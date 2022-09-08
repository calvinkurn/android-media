package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val datasource: ClientMessageDatasource,
    private val orderRoomDatasource: OrderRoomDatasource
) {
    fun getCachedNewOrderList() : Flow<List<OrderWithProduct>>{
        return orderRoomDatasource.getNewOrderList()
    }

    fun getCachedNewOrderCount() : Flow<Int>{
        return orderRoomDatasource.getNewOrderCount()
    }

    fun getCachedReadyToDeliverOrderList() : Flow<List<OrderWithProduct>>{
        return orderRoomDatasource.getReadyToDeliverOrderList()
    }

    fun getCachedReadyToDeliverOrderCount() : Flow<Int>{
        return orderRoomDatasource.getReadyToDeliverOrderCount()
    }

    suspend fun sendMessagesToNodes(action: Action) {
        datasource.sendMessagesToNodes(action)
    }
}