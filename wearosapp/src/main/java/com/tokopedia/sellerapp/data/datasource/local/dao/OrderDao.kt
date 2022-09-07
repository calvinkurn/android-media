package com.tokopedia.sellerapp.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.sellerapp.data.datasource.local.entity.OrderEntity
import com.tokopedia.sellerapp.data.datasource.local.entity.ProductEntity
import com.tokopedia.sellerapp.data.datasource.local.model.OrderModel
import com.tokopedia.sellerapp.data.datasource.remote.OrderListModel
import com.tokopedia.sellerapp.data.mapper.OrderMapper.mapModelToOrderEntity
import com.tokopedia.sellerapp.data.mapper.OrderMapper.mapModelToProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderList(list: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderProducts(list: List<ProductEntity>)

    @Transaction
    @Query("SELECT * FROM WearOrder wo INNER JOIN WearOrderProduct wop " +
            "ON wo.order_id = wop.order_id")
    fun getOrderList() : Flow<List<OrderModel>>
}