package com.tokopedia.sellerapp.data.datasource.remote

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.MessageConstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class OrderRemoteDatasource: MessageClient.OnMessageReceivedListener {
    private val _orderList: MutableStateFlow<String> = MutableStateFlow("")
    val orderList: StateFlow<String>
        get() = _orderList

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when(messageEvent.path) {
            MessageConstant.GET_ORDER_LIST_PATH -> {
                _orderList.value = messageEvent.data.decodeToString()
            }
            MessageConstant.ACCEPT_BULK_ORDER_PATH -> {
                /* nothing */
            }
        }
    }

    suspend fun sendMessagesToNodes(
        action: Action,
        nodeClient: NodeClient,
        messageClient: MessageClient
    ) {
        val nodes = nodeClient.connectedNodes.await()
        nodes.map { node ->
            val message = action.getPath()
            messageClient.sendMessage(node.id, message, byteArrayOf()).await()
        }
    }
}