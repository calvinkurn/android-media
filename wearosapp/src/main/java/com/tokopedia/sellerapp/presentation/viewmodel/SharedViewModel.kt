package com.tokopedia.sellerapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.CapabilityClient
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.domain.interactor.GetNotificationUseCase
import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCase
import com.tokopedia.sellerapp.domain.interactor.OrderUseCaseImpl
import com.tokopedia.sellerapp.domain.model.NotificationModel
import com.tokopedia.sellerapp.domain.interactor.OrderUseCase
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.model.generateInitialMenu
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.CapabilityConstant.CAPABILITY_PHONE_APP
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val orderUseCase: OrderUseCase,
    private val getSummaryUseCase: GetSummaryUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val capabilityClient: CapabilityClient,
    private val clientMessageDatasource: ClientMessageDatasource
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_STOP_TIMEOUT = 3000L
        private const val INDEX_NOT_FOUND = -1
    }

    init {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.GET_ORDER_LIST)
            clientMessageDatasource.sendMessagesToNodes(Action.GET_NOTIFICATION_LIST)
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

    private val _notifications = MutableStateFlow<UiState<List<NotificationModel>>>(UiState.Loading())
    val notifications : StateFlow<UiState<List<NotificationModel>>> = _notifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private val _notificationDetail = MutableStateFlow<UiState<NotificationModel>>(UiState.Loading())
    val notificationDetail : StateFlow<UiState<NotificationModel>> = _notificationDetail.stateIn(
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

    private val _orderDetail = MutableStateFlow<UiState<OrderModel>>(UiState.Loading())
    val orderDetail : StateFlow<UiState<OrderModel>> = _orderDetail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private var _acceptBulkOrder= MutableStateFlow<UiState<Unit>>(UiState.Loading())
    val acceptBulkOrder: StateFlow<UiState<Unit>> = _acceptBulkOrder.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private val _ifPhoneHasApp = MutableStateFlow(false)
    val ifPhoneHasApp: StateFlow<Boolean>
        get() = _ifPhoneHasApp

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

    private fun openNewOrderList() {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_NEW_ORDER_LIST)
        }
    }

    private fun openReadyToShip() {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_READY_TO_SHIP)
        }
    }

    fun getNotificationList() {
        viewModelScope.launch {
            _notifications.emitAll(
                getNotificationUseCase.getNotificationList().map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun getNotificationDetail(notificationId: String) {
        viewModelScope.launch {
            _notificationDetail.emitAll(
                getNotificationUseCase.getNotificationDetail(notificationId).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun getOrderSummary(dataKey: String) {
        launch {
            _orderSummary.emitAll(
                getSummaryUseCase.getOrderSummary(dataKey).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun getOrderList(dataKey: String) {
        launch {
            _orderList.emitAll(
                orderUseCase.getOrderList(dataKey).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun getOrderDetail(orderId: String) {
        launch {
            _orderDetail.emitAll(
                orderUseCase.getOrderDetail(orderId).map {
                    UiState.Success(data = it)
                }
            )
        }
    }

    fun sendRequestAcceptBulkOrder(listOrderId: List<String>) {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.ACCEPT_BULK_ORDER, listOrderId)
        }
    }

    fun resetAcceptBulkOrderState() {
        launch {
            _acceptBulkOrder.emit(UiState.Loading())
        }
    }

    fun checkIfPhoneHasApp() {
        launch {
            try {
                val capabilityInfo = capabilityClient
                    .getCapability(CAPABILITY_PHONE_APP, CapabilityClient.FILTER_ALL)
                    .await()

                // There should only ever be one phone in a node set (much less w/ the correct
                // capability), so I am just grabbing the first one (which should be the only one).
                val nodes = capabilityInfo.nodes
                val androidPhoneNodeWithApp = nodes.firstOrNull { it.isNearby }?.id
                val phoneHasApp = androidPhoneNodeWithApp != null
                _ifPhoneHasApp.value = phoneHasApp

                if (phoneHasApp) {
                    clientMessageDatasource.sendMessagesToNodes(Action.GET_PHONE_STATE)
                }
            } catch (throwable: Throwable) { /* nothing to do */ }
        }
    }

    fun openLoginPageInApp() {
        launch {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_LOGIN_PAGE)
        }
    }

    fun setAcceptOrderSuccess() {
        launch {
            _acceptBulkOrder.emit(UiState.Success())
        }
    }

    fun openOrderPageBasedOnType(orderType: String) {
        if (orderType == DATAKEY_NEW_ORDER) {
            openNewOrderList()
        } else {
            openReadyToShip()
        }
    }

}
