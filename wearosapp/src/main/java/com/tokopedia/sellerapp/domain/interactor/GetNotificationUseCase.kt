package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.domain.model.NotificationModel
import kotlinx.coroutines.flow.Flow

interface GetNotificationUseCase {
    fun getNotificationList(): Flow<List<NotificationModel>>
    fun getNotificationDetail(notificationId: String): Flow<NotificationModel>
}
