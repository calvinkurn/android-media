package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.NotificationRoomDataSource
import com.tokopedia.sellerapp.data.datasource.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationRoomDataSource: NotificationRoomDataSource
) : BaseRepository<List<NotificationEntity>> {

    override fun getCachedData(params: Array<String>): Flow<List<NotificationEntity>> {
        return notificationRoomDataSource.getNotificationList()
    }

    fun getCachedData(notificationId: String): Flow<NotificationEntity> {
        return notificationRoomDataSource.getNotificationById(notificationId)
    }
}