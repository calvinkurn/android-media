package com.tokopedia.sellerapp.presentation.viewmodel

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val immediate: CoroutineDispatcher
    val computation: CoroutineDispatcher
}
