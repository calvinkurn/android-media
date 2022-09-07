package com.tokopedia.sellerapp.data.datasource.local

import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.local.model.OrderWithProduct
import com.tokopedia.sellerapp.data.datasource.remote.OrderListModel
import com.tokopedia.sellerapp.data.mapper.OrderDataMapper.mapModelToOrderEntity
import com.tokopedia.sellerapp.data.mapper.OrderDataMapper.mapModelToProductEntity
import kotlinx.coroutines.flow.Flow

class OrderRoomDatasource(
    private val orderDao: OrderDao
) {
    fun saveOrderList(orderList: OrderListModel) {
        orderDao.insertOrderProducts(orderList.mapModelToProductEntity())
        orderDao.insertOrderList(orderList.mapModelToOrderEntity())
    }

    fun getNewOrderList() : Flow<List<OrderWithProduct>>{
        return orderDao.getNewOrderList()
    }

    //Example
    fun getReadyToDeliverOrderList() : Flow<List<OrderWithProduct>>{
        return orderDao.getNewOrderList()
    }
}