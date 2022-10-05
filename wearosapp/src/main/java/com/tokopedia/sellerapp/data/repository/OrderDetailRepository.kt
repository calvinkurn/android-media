package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.OrderRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderDetailRepository @Inject constructor(
    private val orderRoomDatasource: OrderRoomDatasource
): BaseRepository<OrderWithProduct> {

    override fun getCachedData(params: Array<String>): Flow<OrderWithProduct> {
        return orderRoomDatasource.getOrderDetail(params.first())
    }

}