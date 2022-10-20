package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.data.datasource.local.NotificationRoomDataSource
import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.data.repository.NotificationRepository
import com.tokopedia.sellerapp.data.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideNewOrderRepository(
        datasource: ClientMessageDatasource,
        orderRoomDatasource: OrderRoomDatasource
    ): OrderRepository {
        return OrderRepository(datasource, orderRoomDatasource)
    }

    @Provides
    fun provideNotificationRepository(
        notificationRoomDataSource: NotificationRoomDataSource
    ): NotificationRepository {
        return NotificationRepository(notificationRoomDataSource)
    }
}