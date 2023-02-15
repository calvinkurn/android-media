package com.tokopedia.sellerapp.presentation.viewmodel.data

import com.tokopedia.sellerapp.domain.interactor.GetNotificationUseCase
import com.tokopedia.sellerapp.domain.model.NotificationModel
import com.tokopedia.sellerapp.presentation.viewmodel.util.DummyData.listNotificationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetNotificationUseCase: GetNotificationUseCase {
    override fun getNotificationList(): Flow<List<NotificationModel>> = flow {
        emit(listNotificationData)
    }

    override fun getNotificationDetail(notificationId: String): Flow<NotificationModel> = flow {
        emit(listNotificationData.first { it.notificationId == notificationId })
    }
}
