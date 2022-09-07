package com.tokopedia.sellerapp.di

import android.content.Context
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.data.repository.OrderRepository
import com.tokopedia.sellerapp.data.repository.WearCacheActionImpl
import com.tokopedia.sellerapp.domain.interactor.NewOrderUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WearModule {
    @Provides
    fun provideOrderRoomDatasource(orderDao: OrderDao): OrderRoomDatasource {
        return OrderRoomDatasource(orderDao)
    }

    @Provides
    fun provideMessageClient(
        @ApplicationContext context: Context
    ): MessageClient = Wearable.getMessageClient(context)

    @Provides
    fun provideNodeClient(
        @ApplicationContext context: Context
    ): NodeClient = Wearable.getNodeClient(context)

    @Provides
    fun provideWearCacheImpl(
        orderRoomDatasource: OrderRoomDatasource,
        dispatchers: CoroutineDispatchers
    ) =
        WearCacheActionImpl(dispatchers, orderRoomDatasource)

    @Provides
    fun provideOrderRemoteDatasource(
        wearCacheActionImpl: WearCacheActionImpl,
        nodeClient: NodeClient,
        messageClient: MessageClient
    ) = ClientMessageDatasource(nodeClient, messageClient, wearCacheActionImpl)

    @Provides
    fun provideOrderRepository(
        datasource: ClientMessageDatasource,
        orderRoomDatasource: OrderRoomDatasource
    ): OrderRepository {
        return OrderRepository(datasource, orderRoomDatasource)
    }

    @Provides
    fun provideOrderUseCaseImpl(
        orderRepository: OrderRepository
    ): NewOrderUseCaseImpl {
        return NewOrderUseCaseImpl(orderRepository)
    }
}