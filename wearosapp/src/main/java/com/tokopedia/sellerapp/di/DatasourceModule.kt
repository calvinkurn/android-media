package com.tokopedia.sellerapp.di

import android.content.Context
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerapp.data.datasource.local.NotificationRoomDataSource
//import com.tokopedia.sellerapp.data.datasource.local.NotificationRoomDataSource
import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.SummaryRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.dao.NotificationDao
import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.local.dao.SummaryDao
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.data.repository.WearCacheActionImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
    @Provides
    fun provideOrderRoomDatasource(orderDao: OrderDao): OrderRoomDatasource {
        return OrderRoomDatasource(orderDao)
    }

    @Provides
    fun provideNotificationRoomDataSource(notificationDao: NotificationDao) : NotificationRoomDataSource {
        return NotificationRoomDataSource(notificationDao)
    }

    @Provides
    fun provideSummaryRoomDatasource(summaryDao: SummaryDao): SummaryRoomDatasource {
        return SummaryRoomDatasource(summaryDao)
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
    fun provideCapabilityClient(
        @ApplicationContext context: Context
    ): CapabilityClient = Wearable.getCapabilityClient(context)

    @Provides
    fun provideRemoteActivityHelper(
        @ApplicationContext context: Context
    ): RemoteActivityHelper = RemoteActivityHelper(context)

    @Provides
    fun provideWearCacheActionImpl(
        orderRoomDatasource: OrderRoomDatasource,
        notificationRoomDataSource: NotificationRoomDataSource,
        summaryRoomDatasource: SummaryRoomDatasource,
        dispatchers: CoroutineDispatchers
    ) = WearCacheActionImpl(dispatchers, orderRoomDatasource, notificationRoomDataSource, summaryRoomDatasource)

    @Provides
    fun provideClientMessageDatasource(
        wearCacheActionImpl: WearCacheActionImpl,
        nodeClient: NodeClient,
        messageClient: MessageClient
    ) = ClientMessageDatasource(nodeClient, messageClient, wearCacheActionImpl)
}