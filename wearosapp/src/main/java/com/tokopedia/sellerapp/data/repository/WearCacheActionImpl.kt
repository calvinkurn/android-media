package com.tokopedia.sellerapp.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerapp.data.datasource.local.NotificationRoomDataSource
import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.SummaryRoomDatasource
import com.tokopedia.sellerapp.data.mapper.NotificationDataMapper
import com.tokopedia.sellerapp.data.mapper.OrderDataMapper
import com.tokopedia.sellerapp.data.mapper.SummaryDataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class WearCacheActionImpl(
    private val dispatchers: CoroutineDispatchers,
    private val orderRoomDatasource: OrderRoomDatasource,
    private val notificationRoomDataSource: NotificationRoomDataSource,
    private val summaryRoomDatasource: SummaryRoomDatasource,
) : WearCacheAction, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.io + job

    override fun saveOrderListToCache(message: String) {
        launch {
            orderRoomDatasource.saveOrderList(OrderDataMapper.mapMessageDataToModel(message))
        }
    }

    override fun saveSummaryToCache(message: String) {
        launch {
            summaryRoomDatasource.saveSummary(SummaryDataMapper.mapMessageDataToModel(message))
        }
    }

    override fun saveNotificationListToCache(message: String) {
        launch {
            notificationRoomDataSource.saveNotificationList(NotificationDataMapper.mapMessageDataToModel(message))
        }
    }
}