package com.tokopedia.sellerapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerapp.data.datasource.local.model.OrderModel
import com.tokopedia.sellerapp.data.repository.OrderRepository
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import com.tokopedia.sellerapp.presentation.model.generateInitialMenu
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.MessageConstant.ACCEPT_BULK_ORDER_PATH
import com.tokopedia.sellerapp.util.MessageConstant.GET_ORDER_LIST_PATH
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    val orderRepository: OrderRepository
) : BaseViewModel(dispatchers.io) {

    init {
        viewModelScope.launch { orderRepository.collectMessageReceived() }
    }

    private val _homeMenu: MutableStateFlow<List<MenuItem>> = MutableStateFlow(generateInitialMenu())
    val homeMenu : StateFlow<List<MenuItem>>
        get() = _homeMenu

    val orderList: StateFlow<UiState<List<OrderModel>>> = orderRepository.getCachedOrderList()
        .map {
            updateMenuCounter(it.size, TITLE_NEW_ORDER)
            UiState.Success(data = it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UiState.Idle()
        )

    private val _action: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle())
    val action: StateFlow<UiState<Boolean>>
        get() = _action

    fun sendAction(action: Action, messageClient: MessageClient, nodeClient: NodeClient) {
        launchCatchError(block = {
            _action.value = UiState.Loading()

            orderRepository.sendMessagesToNodes(
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

    private fun updateMenuCounter(count: Int, title: String){
        val newMenu = _homeMenu.value
        val index = newMenu.indexOfFirst { it.title == title }
        newMenu[index].unreadCount = count
        _homeMenu.value = newMenu
    }
}