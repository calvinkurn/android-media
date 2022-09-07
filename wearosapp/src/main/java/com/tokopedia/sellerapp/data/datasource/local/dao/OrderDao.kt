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
    companion object {
        private const val STATUS_NEW_ORDER = 220
        private const val STATUS_READY_TO_DELIVER = 330
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderList(list: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderProducts(list: List<ProductEntity>)

    @Transaction
    @Query("SELECT * FROM WearOrder wo INNER JOIN WearOrderProduct wop " +
            "ON wo.order_id = wop.order_id WHERE wo.order_status_id == $STATUS_NEW_ORDER")
    fun getNewOrderList() : Flow<List<OrderWithProduct>>

    //Example
    @Transaction
    @Query("SELECT * FROM WearOrder wo INNER JOIN WearOrderProduct wop " +
            "ON wo.order_id = wop.order_id WHERE wo.order_status_id == $STATUS_READY_TO_DELIVER")
    fun getReadyToDeliverOrderList() : Flow<List<OrderWithProduct>>
}