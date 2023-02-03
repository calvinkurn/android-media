package com.tokopedia.sellerapp.data.datasource.remote

import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import com.google.gson.Gson
import com.tokopedia.sellerapp.data.repository.WearCacheAction
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.MessageConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ClientMessageDatasource @Inject constructor(
    private val nodeClient: NodeClient,
    private val messageClient: MessageClient,
    private val wearCacheAction: WearCacheAction,
): MessageClient.OnMessageReceivedListener, CoroutineScope {
    var activityMessageListener: ActivityMessageListener? = null

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = messageEvent.data.decodeToString()
        Log.d("TokopediaWearOS", "onMessageReceived: $data")
        when(messageEvent.path) {
            MessageConstant.GET_ORDER_LIST_PATH -> wearCacheAction.saveOrderListToCache(data)
            MessageConstant.GET_SUMMARY_PATH -> wearCacheAction.saveSummaryToCache(data)
            else -> { }
        }
        activityMessageListener?.onMessageReceived(messageEvent)
    }

    suspend fun sendMessagesToNodes(action: Action, data: Any? = null) {
        val nodes = nodeClient.connectedNodes.await()

        nodes.map { node ->
            val message = action.getPath()
            val dataByteArray = Gson().toJson(data).toByteArray()
            messageClient.sendMessage(node.id, message, dataByteArray).await()
        }
    }

    fun addMessageClientListener() {
        messageClient.addListener(this)
    }

    fun removeMessageClientListener() {
        messageClient.removeListener(this)
    }

    fun addActivityMessageListener(activityMessageListener: ActivityMessageListener) {
        this.activityMessageListener = activityMessageListener
    }

    fun removeActivityMessageListener() {
        this.activityMessageListener = null
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()
}

interface ActivityMessageListener {
    fun onMessageReceived(messageEvent: MessageEvent)
}