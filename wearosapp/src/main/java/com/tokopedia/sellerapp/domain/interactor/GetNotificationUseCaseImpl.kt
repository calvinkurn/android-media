package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.NotificationRepository
import com.tokopedia.sellerapp.domain.mapper.NotificationDomainMapper.mapToDomainModel
import com.tokopedia.sellerapp.domain.model.NotificationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotificationUseCaseImpl @Inject constructor(
    private val notificationRepository: NotificationRepository
): GetNotificationUseCase {
    override fun getNotificationList(): Flow<List<NotificationModel>> {
        return notificationRepository.getCachedData().map { entities ->
            entities.mapToDomainModel()
        }
    }
    override fun getNotificationDetail(notificationId: String): Flow<NotificationModel> {
        return notificationRepository.getCachedData(notificationId).map { entity ->
            entity.mapToDomainModel()
        }
    }
}
