package com.tokopedia.sellerapp.data.repository

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.model.OrderModel
import com.tokopedia.sellerapp.data.datasource.remote.OrderRemoteDatasource
import com.tokopedia.sellerapp.data.mapper.OrderMapper.mapMessageDataToModel
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    val orderRemoteDatasource: OrderRemoteDatasource,
    private val orderRoomDatasource: OrderRoomDatasource
) {
    suspend fun collectMessageReceived() {
        orderRemoteDatasource.orderList.collect {
            if(it.isNotEmpty()){
                saveMessageToCache(it)
            }
        }
    }

    fun getCachedOrderList() : Flow<List<OrderModel>>{
        return orderRoomDatasource.getOrderList()
    }

    suspend fun sendMessagesToNodes(
        action: Action,
        nodeClient: NodeClient,
        messageClient: MessageClient
    ) {
        orderRemoteDatasource.sendMessagesToNodes(action, nodeClient, messageClient)
    }

    private fun saveMessageToCache(message: String) {
        orderRoomDatasource.saveOrderList(message.mapMessageDataToModel())
    }
}