package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewOrderRepository @Inject constructor(
    private val clientMessageDatasource: ClientMessageDatasource,
    private val orderRoomDatasource: OrderRoomDatasource
): BaseRepository<List<OrderWithProduct>> {

    override fun getCachedData(): Flow<List<OrderWithProduct>> {
        return orderRoomDatasource.getNewOrderList()
    }

    override fun getCachedDataCount(): Flow<Int> {
        return orderRoomDatasource.getNewOrderCount()
    }

    override suspend fun sendMessagesToNodes(action: Action) {
        clientMessageDatasource.sendMessagesToNodes(action)
    }

}