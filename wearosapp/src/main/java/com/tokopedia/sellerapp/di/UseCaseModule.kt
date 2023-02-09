package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.data.repository.NotificationRepository
import com.tokopedia.sellerapp.data.repository.OrderDetailRepository
import com.tokopedia.sellerapp.data.repository.OrderRepository
import com.tokopedia.sellerapp.data.repository.SummaryRepository
import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCase
import com.tokopedia.sellerapp.domain.interactor.GetSummaryUseCaseImpl
import com.tokopedia.sellerapp.domain.interactor.OrderUseCase
import com.tokopedia.sellerapp.domain.interactor.GetNotificationUseCase
import com.tokopedia.sellerapp.domain.interactor.OrderUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideNewOrderUseCase(
        orderRepository: OrderRepository,
        orderDetailRepository: OrderDetailRepository,
    ): OrderUseCase {
        return OrderUseCaseImpl(orderRepository, orderDetailRepository)
    }

    @Provides
    fun provideGetSummaryUseCase(
        summaryRepository: SummaryRepository
    ): GetSummaryUseCase {
        return GetSummaryUseCaseImpl(summaryRepository)
    }

    @Provides
    fun provideNotificationUseCase(
        notificationRepository: NotificationRepository
    ): GetNotificationUseCase {
        return GetNotificationUseCase(notificationRepository)
    }
}
