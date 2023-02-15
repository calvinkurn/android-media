package com.tokopedia.sellerapp.data.datasource.local

import com.tokopedia.sellerapp.data.datasource.local.dao.NotificationDao
import com.tokopedia.sellerapp.data.datasource.local.entity.NotificationEntity
import com.tokopedia.sellerapp.data.datasource.remote.NotificationListModel
import com.tokopedia.sellerapp.data.mapper.NotificationDataMapper.mapModelToNotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRoomDataSource(private val notificationDao: NotificationDao) {

    fun saveNotificationList(notificationListModel: NotificationListModel) {
        notificationDao.insertNotificationList(notificationListModel.mapModelToNotificationEntity())
    }

    fun getNotificationList(): Flow<List<NotificationEntity>> {
        return notificationDao.getNotificationList()
    }

    fun getNotificationById(notificationId: String): Flow<NotificationEntity> {
        return notificationDao.getNotificationById(notificationId)
    }
}