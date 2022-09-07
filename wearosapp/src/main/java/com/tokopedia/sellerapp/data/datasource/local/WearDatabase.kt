package com.tokopedia.sellerapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity

@Database(entities = [OrderEntity::class, ProductEntity::class], version = 5, exportSchema = false)
abstract class WearDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao

    companion object {
        const val DATABASE_NAME = "tokopedia_wear"
    }
}