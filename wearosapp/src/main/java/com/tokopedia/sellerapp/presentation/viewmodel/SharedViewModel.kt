package com.tokopedia.sellerapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCase
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.interactor.NewOrderUseCase
import com.tokopedia.sellerapp.domain.interactor.ReadyToDeliverOrderUseCase
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.model.generateInitialMenu
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val newOrderUseCase: NewOrderUseCase,
    private val readyToDeliverOrderUseCase: ReadyToDeliverOrderUseCase,
    private val getSummaryUseCase: GetSummaryUseCase,
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_STOP_TIMEOUT = 3000L
        private const val INDEX_NOT_FOUND = -1
    }

    val homeMenu: StateFlow<List<MenuItem>> = getSummaryUseCase.getMenuItemCounter().map {
        getUpdatedMenuCounter(it)
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

    private fun getUpdatedMenuCounter(listSummary: List<SummaryModel>) : List<MenuItem> {
        return homeMenu.value.toMutableList().apply {
            listSummary.forEach { summaryModel ->
                val index = indexOfFirst { it.title == summaryModel.title }
                if(index != INDEX_NOT_FOUND){
                    this[index] = this[index].copy(unreadCount = summaryModel.counter.toIntOrZero())
                }
            }
        }
    }
}