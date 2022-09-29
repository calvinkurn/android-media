package com.tokopedia.sellerapp.presentation.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.interactor.NewOrderUseCase
import com.tokopedia.sellerapp.domain.interactor.ReadyToDeliverOrderUseCase
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.model.generateInitialMenu
import com.tokopedia.sellerapp.util.CapabilityConstant.CAPABILITY_PHONE_APP
import com.tokopedia.sellerapp.util.MarketURIConstant.MARKET_TOKOPEDIA
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val newOrderUseCase: NewOrderUseCase,
    private val readyToDeliverOrderUseCase: ReadyToDeliverOrderUseCase,
    private val capabilityClient: CapabilityClient,
    private val remoteActivityHelper: RemoteActivityHelper,
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_STOP_TIMEOUT = 3000L
        private const val INDEX_NOT_FOUND = -1
    }

    val homeMenu: StateFlow<List<MenuItem>> = merge(
        newOrderUseCase.getCount(),
        readyToDeliverOrderUseCase.getCount()
    ).map {
        getUpdatedMenuCounter(it.first, it.second)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = generateInitialMenu()
    )

    val newOrderList: StateFlow<UiState<List<OrderModel>>> = newOrderUseCase.getOrderList().map {
        UiState.Success(data = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    val readyToDeliverOrderList: StateFlow<UiState<List<OrderModel>>> = readyToDeliverOrderUseCase.getOrderList().map {
        UiState.Success(data = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
        initialValue = UiState.Idle()
    )

    private val _action: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle())
    val action: StateFlow<UiState<Boolean>>
        get() = _action

    fun sendRequest() {
        launchCatchError(block = {
            _action.value = UiState.Loading()

            // call usecase method

            _action.value = UiState.Success()
        }, onError = { throwable ->
            _action.value = UiState.Fail(
                throwable = throwable
            )
        })
    }

    private fun getUpdatedMenuCounter(title: String, count: Int) : List<MenuItem> {
        return homeMenu.value.toMutableList().apply {
            val index = indexOfFirst { it.title == title }
            if(index != INDEX_NOT_FOUND){
                this[index] = this[index].copy(unreadCount = count)
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
        }
    }
}