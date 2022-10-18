package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.data.datasource.remote.AcceptBulkOrderModel
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val clientMessageDatasource: ClientMessageDatasource,
    private val orderRoomDatasource: OrderRoomDatasource
): BaseRepository<List<OrderWithProduct>> {

    override fun getCachedData(params: Array<String>): Flow<List<OrderWithProduct>> {
        return orderRoomDatasource.getOrderList(params)
    }

    override fun getCachedDataCount(params: Array<String>): Flow<Int> {
        return orderRoomDatasource.getOrderCount(params)
    }

    override suspend fun sendMessagesToNodes(action: Action) {
        clientMessageDatasource.sendMessagesToNodes(action)
    }

}