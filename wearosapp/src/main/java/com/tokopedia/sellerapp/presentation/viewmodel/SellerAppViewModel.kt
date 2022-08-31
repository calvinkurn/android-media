package com.tokopedia.sellerapp.presentation.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import com.tokopedia.sellerapp.util.StringConstant.MESSAGE_CLIENT_APP_DETECTION
import com.tokopedia.sellerapp.util.StringConstant.MESSAGE_CLIENT_START_ORDER_ACTIVITY
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class SellerAppViewModel(
    private val componentActivity: ComponentActivity
): ViewModel() {

    private val orderListData = MutableLiveData<String>()

    fun getOrderListData(action: Action) {
        val nodeClient = Wearable.getNodeClient(componentActivity)
        val messageClient = Wearable.getMessageClient(componentActivity)

        messageClient.addListener { messageEvent ->
            when(messageEvent.path) {
                MESSAGE_CLIENT_START_ORDER_ACTIVITY ->
                    orderListData.postValue(messageEvent.data.decodeToString())
//                    MESSAGE_CLIENT_APP_DETECTION -> appDetectedCallback()
            }
        }

        componentActivity.lifecycleScope.launch {
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

    fun getOrderListDataState(): LiveData<String> = orderListData

    enum class Action {
        GET_ORDER_LIST, DETECT_APP_INSTALLED
    }
}