package com.tokopedia.sellerapp.presentation.viewmodel.util

import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(CoroutineTestDispatchersProvider.main)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}