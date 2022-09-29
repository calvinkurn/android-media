package com.tokopedia.sellerapp.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerapp.data.datasource.local.NotificationRoomDataSource
import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.mapper.OrderDataMapper.mapMessageDataToModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class WearCacheActionImpl(
    private val dispatchers: CoroutineDispatchers,
    private val orderRoomDatasource: OrderRoomDatasource,
    private val notificationRoomDataSource: NotificationRoomDataSource,
) : WearCacheAction, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.io + job

    override fun saveOrderListToCache(message: String) {
        launch {
            orderRoomDatasource.saveOrderList(message.mapMessageDataToModel())
        }
    }

    override fun saveOrderSummaryToCache(message: String) {
        launch {
            //to be added
        }
    }

    override fun saveNotificationListToCache(message: String) {
        launch {
//            notificationRoomDataSource.s
        }
    }
}