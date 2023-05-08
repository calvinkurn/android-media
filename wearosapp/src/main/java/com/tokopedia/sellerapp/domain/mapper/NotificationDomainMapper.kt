package com.tokopedia.sellerapp.domain.mapper

import com.tokopedia.sellerapp.data.datasource.local.entity.NotificationEntity
import com.tokopedia.sellerapp.domain.model.NotificationModel

object NotificationDomainMapper {

    fun List<NotificationEntity>.mapToDomainModel(): List<NotificationModel> {
        return map { mapEntityToDomainModel(it) }
    }

    fun NotificationEntity.mapToDomainModel(): NotificationModel {
        return mapEntityToDomainModel(this)
    }

    private fun mapEntityToDomainModel(notificationEntity: NotificationEntity): NotificationModel {
        return NotificationModel(
            notificationId = notificationEntity.notificationId,
            readStatus = notificationEntity.readStatus,
            status = notificationEntity.status,
            title = notificationEntity.title,
            shortDescription = notificationEntity.shortDescription,
            createTimeUnix = notificationEntity.createTimeUnix,
            infoThumbnailUrl = notificationEntity.infoThumbnailUrl
        )
    }
}
