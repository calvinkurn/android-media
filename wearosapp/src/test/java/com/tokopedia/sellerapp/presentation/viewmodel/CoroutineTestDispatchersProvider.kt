package com.tokopedia.sellerapp.presentation.viewmodel

import kotlinx.coroutines.Dispatchers

object CoroutineTestDispatchersProvider: CoroutineDispatchers {

    override val main = Dispatchers.Unconfined

    override val io = Dispatchers.Unconfined

    override val default = Dispatchers.Unconfined

    override val immediate = Dispatchers.Unconfined

    override val computation = Dispatchers.Unconfined
}
