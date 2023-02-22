package com.tokopedia.sellerapp.di

import android.content.Context
import androidx.room.Room
import com.tokopedia.sellerapp.data.datasource.local.WearDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        WearDatabase::class.java,
        WearDatabase.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideOrderDao(database: WearDatabase) = database.orderDao()

    @Singleton
    @Provides
    fun provideSummaryDao(database: WearDatabase) = database.summaryDao()

}