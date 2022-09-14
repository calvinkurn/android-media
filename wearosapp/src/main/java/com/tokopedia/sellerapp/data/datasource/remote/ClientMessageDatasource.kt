package com.tokopedia.sellerapp.data.datasource.remote

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.data.repository.WearCacheAction
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.MessageConstant
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

open class ClientMessageDatasource @Inject constructor(
    private val nodeClient: NodeClient,
    private val messageClient: MessageClient,
    private val wearCacheAction: WearCacheAction
): MessageClient.OnMessageReceivedListener {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = messageEvent.data.decodeToString()
        when(messageEvent.path) {
            MessageConstant.GET_ORDER_LIST_PATH -> wearCacheAction.saveOrderListToCache(data)
            else -> { }
        }
    }

    suspend fun sendMessagesToNodes(action: Action) {
        val nodes = nodeClient.connectedNodes.await()
        nodes.map { node ->
            val message = action.getPath()
            messageClient.sendMessage(node.id, message, byteArrayOf()).await()
        }
    }

    fun addMessageClientListener() {
        messageClient.addListener(this)
    }

    fun removeMessageClientListener() {
        messageClient.removeListener(this)
    }
}