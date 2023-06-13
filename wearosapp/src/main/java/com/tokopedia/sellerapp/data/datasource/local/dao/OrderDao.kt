package com.tokopedia.sellerapp.data.datasource.local.dao

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Query
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity
import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderList(list: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderProducts(list: List<ProductEntity>)

    @Transaction
    @Query("SELECT * FROM WearOrder wo WHERE wo.order_status_id IN (:status)")
    fun getOrderList(status: Array<String>) : Flow<List<OrderWithProduct>>

    @Transaction
    @Query("SELECT * FROM WearOrder wo WHERE wo.order_id IS (:orderId)")
    fun getOrderDetail(orderId: String) : Flow<OrderWithProduct>

    @Query("SELECT count(*) FROM WearOrder wo WHERE wo.order_status_id IN (:status)")
    fun getOrderCount(status: Array<String>) : Flow<Int>

    @Query("DELETE FROM WearOrder WHERE order_status_id IN (:status)")
    fun clear(status: Array<String>)

}
