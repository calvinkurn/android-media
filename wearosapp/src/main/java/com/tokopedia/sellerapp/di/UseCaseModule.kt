package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.data.repository.NewOrderRepository
import com.tokopedia.sellerapp.data.repository.ReadyToDeliverOrderRepository
import com.tokopedia.sellerapp.domain.interactor.NewOrderUseCase
import com.tokopedia.sellerapp.domain.interactor.ReadyToDeliverOrderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideNewOrderUseCase(
        newOrderRepository: NewOrderRepository
    ): NewOrderUseCase {
        return NewOrderUseCase(newOrderRepository)
    }

    @Provides
    fun provideReadyToDeliverOrderUseCase(
        readyToDeliverOrderRepository: ReadyToDeliverOrderRepository
    ): ReadyToDeliverOrderUseCase {
        return ReadyToDeliverOrderUseCase(readyToDeliverOrderRepository)
    }
}