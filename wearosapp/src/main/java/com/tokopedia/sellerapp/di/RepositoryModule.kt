package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.data.repository.NewOrderRepository
import com.tokopedia.sellerapp.data.repository.ReadyToDeliverOrderRepository
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
    ): NewOrderRepository {
        return NewOrderRepository(datasource, orderRoomDatasource)
    }

    @Provides
    fun provideReadyToDeliverOrderRepository(
        datasource: ClientMessageDatasource,
        orderRoomDatasource: OrderRoomDatasource
    ): ReadyToDeliverOrderRepository {
        return ReadyToDeliverOrderRepository(datasource, orderRoomDatasource)
    }
}