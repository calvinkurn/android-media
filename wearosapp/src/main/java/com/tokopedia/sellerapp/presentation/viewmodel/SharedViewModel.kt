package com.tokopedia.sellerapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.interactor.NewOrderUseCaseImpl
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import com.tokopedia.sellerapp.presentation.model.generateInitialMenu
import com.tokopedia.sellerapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val newOrderUseCaseImpl: NewOrderUseCaseImpl
) : BaseViewModel(dispatchers.io) {

    private val _homeMenu: MutableStateFlow<List<MenuItem>> = MutableStateFlow(generateInitialMenu())
    val homeMenu : StateFlow<List<MenuItem>>
        get() = _homeMenu

    val newOrderList: StateFlow<UiState<List<OrderModel>>> = newOrderUseCaseImpl().map {
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

    fun sendRequest() {
        launchCatchError(block = {
            _action.value = UiState.Loading()

            newOrderUseCaseImpl.sendRequest()

            _action.value = UiState.Success()
        }, onError = { throwable ->
            _action.value = UiState.Fail(
                throwable = throwable
            )
        })
    }

    private fun updateMenuCounter(count: Int, title: String){
        homeMenu.value.toMutableList().also { listMenu ->
            val index = listMenu.indexOfFirst { it.title == title }
            listMenu[index] = listMenu[index].copy(unreadCount = count)
            _homeMenu.value = listMenu
        }
    }
}