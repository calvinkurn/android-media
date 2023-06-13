package com.tokopedia.sellerapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tokopedia.sellerapp.data.datasource.local.dao.NotificationDao
import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.local.dao.SummaryDao
import com.tokopedia.sellerapp.data.datasource.local.entity.NotificationEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity

@Database(
    entities = [
        OrderEntity::class,
        NotificationEntity::class,
        ProductEntity::class,
        SummaryEntity::class
    ], version = 28, exportSchema = false
)
abstract class WearDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao
    abstract fun summaryDao(): SummaryDao

    companion object {
        const val DATABASE_NAME = "tokopedia_wear"
    }
}