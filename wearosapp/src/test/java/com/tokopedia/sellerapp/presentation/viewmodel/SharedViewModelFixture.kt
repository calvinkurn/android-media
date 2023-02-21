package com.tokopedia.sellerapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.wearable.CapabilityClient
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.domain.interactor.GetNotificationUseCase
import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCase
import com.tokopedia.sellerapp.domain.interactor.OrderUseCase
import com.tokopedia.sellerapp.presentation.viewmodel.data.FakeGetNotificationUseCase
import com.tokopedia.sellerapp.presentation.viewmodel.data.FakeOrderUseCase
import com.tokopedia.sellerapp.presentation.viewmodel.data.FakeSummaryUseCase
import com.tokopedia.sellerapp.presentation.viewmodel.util.MainDispatcherRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SharedViewModelFixture {

    protected lateinit var viewModel : SharedViewModel

    private val orderUseCase: OrderUseCase = FakeOrderUseCase()
    private val getSummaryUseCase: GetSummaryUseCase = FakeSummaryUseCase()
    private val getNotificationUseCase: GetNotificationUseCase = FakeGetNotificationUseCase()

    @RelaxedMockK
    lateinit var capabilityClient: CapabilityClient
    @RelaxedMockK
    lateinit var clientMessageDatasource: ClientMessageDatasource

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SharedViewModel(
            dispatchers = CoroutineTestDispatchersProvider,
            orderUseCase = orderUseCase,
            getSummaryUseCase = getSummaryUseCase,
            capabilityClient = capabilityClient,
            clientMessageDatasource = clientMessageDatasource,
            getNotificationUseCase = getNotificationUseCase
        )
    }
}
