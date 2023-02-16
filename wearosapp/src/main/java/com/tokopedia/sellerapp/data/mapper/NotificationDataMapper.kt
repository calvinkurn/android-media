package com.tokopedia.sellerapp.data.mapper

import com.google.gson.Gson
import com.tokopedia.sellerapp.data.datasource.local.entity.NotificationEntity
import com.tokopedia.sellerapp.data.datasource.remote.NotificationListModel

object NotificationDataMapper {

    fun mapMessageDataToModel(message: String): NotificationListModel {
        return Gson().fromJson(message, NotificationListModel::class.java)
    }

    fun NotificationListModel.mapModelToNotificationEntity(): List<NotificationEntity> {
        return notifications.map {
            NotificationEntity(
                notificationId = it.notificationId,
                readStatus = it.readStatus,
                status = it.status,
                title = it.title,
                shortDescription = it.shortDescription,
                createTimeUnix = it.createTimeUnix,
                infoThumbnailUrl = it.dataNotification.infoThumbnailUrl
            )
        }
    }
}
