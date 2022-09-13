package com.tokopedia.sellerapp.presentation.viewmodel

import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCase
import com.tokopedia.sellerapp.data.datasource.remote.AcceptBulkOrderModel
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.interactor.OrderUseCaseImpl
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.STATUS_NEW_ORDER
import com.tokopedia.sellerapp.domain.model.PhoneState
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.model.generateInitialMenu
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.CapabilityConstant.CAPABILITY_PHONE_APP
import com.tokopedia.sellerapp.util.MarketURIConstant.MARKET_TOKOPEDIA
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val orderUseCaseImpl: OrderUseCaseImpl,
    private val getSummaryUseCase: GetSummaryUseCase,
    private val getOrderUseCase: OrderUseCaseImpl,
    private val capabilityClient: CapabilityClient,
    private val remoteActivityHelper: RemoteActivityHelper,
    private val clientMessageDatasource: ClientMessageDatasource
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_STOP_TIMEOUT = 3000L
        private const val INDEX_NOT_FOUND = -1
    }

    init {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.GET_ORDER_LIST)
            clientMessageDatasource.sendMessagesToNodes(Action.GET_SUMMARY)
        }
    }

    val homeMenu: StateFlow<List<MenuItem>> = getSummaryUseCase.getMenuItemCounter().map {
        getUpdatedMenuCounter(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = generateInitialMenu()
    )

    private val _orderList = MutableStateFlow<UiState<List<OrderModel>>>(UiState.Loading())
    val orderList : StateFlow<UiState<List<OrderModel>>> = _orderList.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private val _orderSummary = MutableStateFlow<UiState<SummaryModel>>(UiState.Loading())
    val orderSummary : StateFlow<UiState<SummaryModel>> = _orderSummary.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private val _currentState = MutableStateFlow<UiState<PhoneState>>(UiState.Loading())
    val currentState : StateFlow<UiState<PhoneState>> = _currentState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private val _orderDetail = MutableStateFlow<UiState<OrderModel>>(UiState.Loading())
    val orderDetail : StateFlow<UiState<OrderModel>> = _orderDetail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )


    init {
//        launch {
//            newOrderUseCase.getAcceptBulkOrder().collectLatest {
//                _acceptBulkOrder.value = UiState.Success(data = it)
//            }
//        }
    }
    private var _acceptBulkOrder= MutableStateFlow<UiState<AcceptBulkOrderModel>>(UiState.Idle())
    val acceptBulkOrder: StateFlow<UiState<AcceptBulkOrderModel>> = _acceptBulkOrder

    fun sendRequest(action: Action, listIds: List<String>) {
    fun checkPhoneState() {
        viewModelScope.launch {
            clientMessageDatasource.sendMessagesToNodes(Action.GET_PHONE_STATE)
        }
    }

    fun getOrderSummary(dataKey: String) {
        viewModelScope.launch {
            _orderSummary.emitAll(
                getSummaryUseCase.getOrderSummary(dataKey).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun getOrderList(dataKey: String) {
        viewModelScope.launch {
            _orderList.emitAll(
                orderUseCaseImpl.getOrderList(dataKey).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun getOrderDetail(orderId: String) {
        viewModelScope.launch {
            _orderDetail.emitAll(
                orderUseCaseImpl.getOrderDetail(orderId).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun sendRequest() {
        launchCatchError(block = {
            newOrderUseCase.sendRequest(action, listIds)
        }, onError = { throwable ->
            throwable
            println(throwable)
        })
    }

    private fun getUpdatedMenuCounter(listSummary: List<SummaryModel>) : List<MenuItem> {
        return homeMenu.value.toMutableList().apply {
            listSummary.forEach { summaryModel ->
                val index = indexOfFirst { it.title == summaryModel.title }
                if(index != INDEX_NOT_FOUND){
                    this[index] = this[index].copy(
                        unreadCount = summaryModel.counter.toIntOrZero(),
                        dataKey = summaryModel.dataKey
                    )
                }
            }
        }
    }

    private val _ifPhoneHasApp = MutableStateFlow(false)
    val ifPhoneHasApp: StateFlow<Boolean>
        get() = _ifPhoneHasApp

    fun checkIfPhoneHasApp() {
        launch {
            try {
                val capabilityInfo = capabilityClient
                    .getCapability(CAPABILITY_PHONE_APP, CapabilityClient.FILTER_ALL)
                    .await()

                withContext(dispatchers.main) {
                    // There should only ever be one phone in a node set (much less w/ the correct
                    // capability), so I am just grabbing the first one (which should be the only one).
                    val nodes = capabilityInfo.nodes
                    val androidPhoneNodeWithApp =
                        nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
                    _ifPhoneHasApp.value = androidPhoneNodeWithApp != null
                }
            } catch (cancellationException: CancellationException) {
                // Request was cancelled normally
            } catch (throwable: Throwable) {

            }
        }
    }

    fun openAppInStoreOnPhone() {
        val intent = Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse(MARKET_TOKOPEDIA))

        launch {
            startRemoteActivity(remoteActivityHelper, intent)
        }
    }

    fun openLoginPageInApp() {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_LOGIN_PAGE)
        }
    }

    private suspend fun startRemoteActivity(
        remoteActivityHelper: RemoteActivityHelper,
        intent: Intent,
    ) {
        try {
            remoteActivityHelper.startRemoteActivity(intent).await()
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
            throw cancellationException
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}