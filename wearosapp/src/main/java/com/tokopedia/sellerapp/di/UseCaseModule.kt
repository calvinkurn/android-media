package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.data.repository.NewOrderRepository
import com.tokopedia.sellerapp.data.repository.ReadyToShipOrderRepository
import com.tokopedia.sellerapp.domain.interactor.NewOrderUseCase
import com.tokopedia.sellerapp.domain.interactor.ReadyToShipOrderUseCase
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
    fun provideReadyToShipOrderUseCase(
        readyToShipOrderRepository: ReadyToShipOrderRepository
    ): ReadyToShipOrderUseCase {
        return ReadyToShipOrderUseCase(readyToShipOrderRepository)
    }
}