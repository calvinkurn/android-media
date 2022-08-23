package com.tokopedia.sellerapp.util

import android.content.ContentValues
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class WearMessageHelper {
    companion object {
        const val MESSAGE_CLIENT_START_ORDER_ACTIVITY = "/start-order-activity"
        const val MESSAGE_CLIENT_APP_DETECTION = "/app-detection"

        fun sendAction(context: ComponentActivity, action: Action, getOrderListDataCallback: (data: String) -> Unit) {
            val nodeClient = Wearable.getNodeClient(context)
            val messageClient = Wearable.getMessageClient(context)

            messageClient.addListener { messageEvent ->
                when(messageEvent.path) {
                    MESSAGE_CLIENT_START_ORDER_ACTIVITY ->
                        getOrderListDataCallback(messageEvent.data.decodeToString())
//                    MESSAGE_CLIENT_APP_DETECTION -> appDetectedCallback()
                }
            }

            context.lifecycleScope.launch {
                try {
                    val nodes = nodeClient.connectedNodes.await()

                    nodes.map { node ->
                        async {
                            val key = when(action) {
                                Action.GET_ORDER_LIST -> MESSAGE_CLIENT_START_ORDER_ACTIVITY
                                Action.DETECT_APP_INSTALLED -> MESSAGE_CLIENT_APP_DETECTION
                            }
                            messageClient.sendMessage(node.id, key, byteArrayOf())
                                .await()
                        }
                    }.awaitAll()

                } catch (cancellationException: CancellationException) {
                    throw cancellationException
                } catch (exception: Exception) {
                    Log.d(ContentValues.TAG, "Starting activity failed: $exception")
                }
            }
        }
    }

    enum class Action {
        GET_ORDER_LIST, DETECT_APP_INSTALLED
    }
}