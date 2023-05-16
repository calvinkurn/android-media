package com.tokopedia.sellerapp.presentation.viewmodel

import com.tokopedia.sellerapp.presentation.viewmodel.CoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
object CoroutineTestDispatchers: CoroutineDispatchers {

    val coroutineDispatcher = TestCoroutineDispatcher()

    override val main = coroutineDispatcher

    override val io = coroutineDispatcher

    override val default = coroutineDispatcher

    override val immediate = coroutineDispatcher

    override val computation = coroutineDispatcher
}
