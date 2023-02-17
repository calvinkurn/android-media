@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.sellerapp.presentation.viewmodel

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.tokopedia.sellerapp.domain.mapper.OrderDomainMapper.STATUS_NEW_ORDER
import com.tokopedia.sellerapp.presentation.viewmodel.data.FakeCapabilityInfo
import com.tokopedia.sellerapp.presentation.viewmodel.data.FakeNode
import com.tokopedia.sellerapp.presentation.viewmodel.util.DataMapper.getUpdatedMenuCounter
import com.tokopedia.sellerapp.presentation.viewmodel.util.DummyData.listNotificationData
import com.tokopedia.sellerapp.presentation.viewmodel.util.DummyData.listOrderData
import com.tokopedia.sellerapp.presentation.viewmodel.util.DummyData.listSummaryData
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_READY_TO_SHIP
import com.tokopedia.sellerapp.util.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest: SharedViewModelFixture() {

    @Test
    fun `when calling getNotificationList, it should get expected list`() = runBlocking {
        viewModel.getNotificationList()

        val expectedResult = listNotificationData

        val actualResult = viewModel.notifications.first().data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling getNotificationDetail, it should get object with the same notificationId`() = runBlocking {
        val notificationId = "122113"

        viewModel.getNotificationDetail(
            notificationId = notificationId
        )

        val expectedResult = listNotificationData.first { it.notificationId == notificationId }

        val actualResult = viewModel.notificationDetail.first().data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling openOrderPageBasedOnType, it should call sendMessagesToNodes function with OPEN_NEW_ORDER_LIST action`() = runBlocking {
        viewModel.openOrderPageBasedOnType(DATAKEY_NEW_ORDER)

        coVerify {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_NEW_ORDER_LIST)
        }
    }

    @Test
    fun `when calling openOrderPageBasedOnType, it should call sendMessagesToNodes function with OPEN_READY_TO_SHIP action`() = runBlocking {
        viewModel.openOrderPageBasedOnType(DATAKEY_READY_TO_SHIP)

        coVerify {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_READY_TO_SHIP)
        }
    }

    @Test
    fun `when calling sendRequestAcceptBulkOrder, it should call sendMessagesToNodes function with ACCEPT_BULK_ORDER action`() = runBlocking {
        viewModel.sendRequestAcceptBulkOrder(listOf("10"))

        coVerify {
            clientMessageDatasource.sendMessagesToNodes(Action.ACCEPT_BULK_ORDER, any())
        }
    }

    @Test
    fun `when calling openLoginPageInApp, it should call sendMessagesToNodes function with OPEN_LOGIN_PAGE action`() = runBlocking {
        viewModel.openLoginPageInApp()

        coVerify {
            clientMessageDatasource.sendMessagesToNodes(Action.OPEN_LOGIN_PAGE)
        }
    }

    @Test
    fun `when calling setAcceptBulkOrderState, it should reset state to success state`() = runBlocking {
        viewModel.setAcceptOrderSuccess()

        assertTrue(viewModel.acceptBulkOrder.first() is UiState.Success)
    }

    @Test
    fun `when calling resetAcceptBulkOrderState, it should reset state to loading state`() = runBlocking {
        viewModel.resetAcceptBulkOrderState()

        assertTrue(viewModel.acceptBulkOrder.first() is UiState.Loading)
    }

    @Test
    fun `when calling the getOrderSummary, it should get object with the same dataKey`() = runBlocking {
        val dataKey = "111"

        val expectedResult = listSummaryData.first { it.dataKey == dataKey }

        viewModel.getOrderSummary(dataKey)

        val actualResult = viewModel.orderSummary.first().data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when collecting homeMenu, it should generate initial menu list`() = runBlocking {
        val expectedResult = getUpdatedMenuCounter(
            homeMenu = viewModel.homeMenu.value,
            listSummary = listSummaryData
        )

        val actualResult = viewModel.homeMenu.first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling the getOrderList, it should get list with the same status order`() = runBlocking {
        val expectedResult = listOrderData.filter { it.orderStatusId in STATUS_NEW_ORDER }

        viewModel.getOrderList(DATAKEY_NEW_ORDER)

        val actualResult = viewModel.orderList.first().data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling the getOrderDetail, it should get object with the same orderId`() = runBlocking {
        val orderId = "10"

        val expectedResult = listOrderData.first { it.orderId == orderId }

        viewModel.getOrderDetail(orderId)

        val actualResult = viewModel.orderDetail.first().data

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling the checkIfPhoneHasApp with the first node is nearby, it should return true value`()= runBlocking {
        val nodes = mutableSetOf(
            FakeNode(
                id = "122",
                displayName = "node 1",
                isNearby = true
            ),
            FakeNode(
                id = "333",
                displayName = "node 2",
                isNearby = false
            )
        )

        val expectedResult = nodes.any { it.isNearby }

        val task: Task<CapabilityInfo> = Tasks.forResult(
            FakeCapabilityInfo(
                nodes = nodes as MutableSet<Node>
            )
        )

        coEvery {
            capabilityClient.getCapability(any(), any())
        } returns task

        viewModel.checkIfPhoneHasApp()

        val actualResult = viewModel.ifPhoneHasApp.first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling the checkIfPhoneHasApp with the second node is nearby, it should return true value`()= runBlocking {
        val nodes = mutableSetOf(
            FakeNode(
                id = "122",
                displayName = "node 1",
                isNearby = false
            ),
            FakeNode(
                id = "333",
                displayName = "node 2",
                isNearby = true
            )
        )

        val expectedResult = nodes.any { it.isNearby }

        val task: Task<CapabilityInfo> = Tasks.forResult(
            FakeCapabilityInfo(
                nodes = nodes as MutableSet<Node>
            )
        )

        coEvery {
            capabilityClient.getCapability(any(), any())
        } returns task

        viewModel.checkIfPhoneHasApp()

        val actualResult = viewModel.ifPhoneHasApp.first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling the checkIfPhoneHasApp with no one node is nearby, it should return false value`()= runBlocking {
        val nodes = mutableSetOf<Node>()

        val task: Task<CapabilityInfo> = Tasks.forResult(
            FakeCapabilityInfo(
                nodes = mutableSetOf()
            )
        )

        val expectedResult = nodes.any { it.isNearby }

        coEvery {
            capabilityClient.getCapability(any(), any())
        } returns task

        viewModel.checkIfPhoneHasApp()

        val actualResult = viewModel.ifPhoneHasApp.first()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calling the checkIfPhoneHasApp but getting throwable, it should have the same value as before`()= runBlocking {
        val expectedResult = false

        coEvery {
            capabilityClient.getCapability(any(), any())
        } throws Throwable()

        viewModel.checkIfPhoneHasApp()

        val actualResult = viewModel.ifPhoneHasApp.first()

        assertEquals(expectedResult, actualResult)
    }
}
