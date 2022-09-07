package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.remote.OrderRemoteDatasource
import com.tokopedia.sellerapp.data.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WearModule {
    @Provides
    fun provideOrderRoomDatasource(orderDao: OrderDao): OrderRoomDatasource {
        return OrderRoomDatasource(orderDao)
    }

    @Provides
    fun provideOrderRemoteDatasource() = OrderRemoteDatasource()

    @Provides
    fun provideOrderRepository(
        orderRoomDatasource: OrderRoomDatasource,
        orderRemoteDatasource: OrderRemoteDatasource
    ): OrderRepository {
        return OrderRepository(orderRemoteDatasource, orderRoomDatasource)
    }
}