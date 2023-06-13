package com.tokopedia.sellerapp.di

import com.tokopedia.sellerapp.presentation.viewmodel.CoroutineDispatchers
import com.tokopedia.sellerapp.presentation.viewmodel.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Singleton
    @Provides
    fun provideHomeDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider
}
