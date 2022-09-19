package com.tokopedia.sellerapp.presentation.viewmodel

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.MessageConstant.ACCEPT_BULK_ORDER_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_NOTIFICATION_LIST_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_ORDER_LIST_PATH
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io),
    MessageClient.OnMessageReceivedListener
{
    private val _orderList: MutableStateFlow<UiState<List<String>>> = MutableStateFlow(UiState.Idle())
    val orderList: StateFlow<UiState<List<String>>>
        get() = _orderList

    private val _notificationList: MutableStateFlow<UiState<List<String>>> = MutableStateFlow(UiState.Idle())
    val notificationList: StateFlow<UiState<List<String>>>
    get() = _notificationList

    private val _action: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle())
    val action: StateFlow<UiState<Boolean>>
        get() = _action

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when(messageEvent.path) {
            GET_ORDER_LIST_PATH -> {
                _orderList.value = UiState.Success(
                    data = listOf(messageEvent.data.decodeToString())
                )
            }
            GET_NOTIFICATION_LIST_PATH -> {
                _notificationList.value = UiState.Success(
                    data = listOf(messageEvent.data.decodeToString())
                )
            }
            ACCEPT_BULK_ORDER_PATH -> {
                /* nothing */
            }
        }
    }

    private suspend fun sendMessagesToNodes(
        action: Action,
        nodeClient: NodeClient,
        messageClient: MessageClient
    ) {
        val nodes = nodeClient.connectedNodes.await()
        nodes.map { node ->
            async {
                val message = action.getPath()
                messageClient.sendMessage(node.id, message, byteArrayOf()).await()
            }
        }.awaitAll()
    }

    fun sendAction(action: Action, messageClient: MessageClient, nodeClient: NodeClient) {
        launchCatchError(block = {
            _action.value = UiState.Loading()

            sendMessagesToNodes(
                action = action,
                messageClient = messageClient,
                nodeClient = nodeClient
            )

            _action.value = UiState.Success()
        }, onError = { throwable ->
            _action.value = UiState.Fail(
                throwable = throwable
            )
        })
    }
}