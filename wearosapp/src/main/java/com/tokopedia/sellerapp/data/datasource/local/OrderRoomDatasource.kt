package com.tokopedia.sellerapp.data.datasource.local

import com.tokopedia.sellerapp.data.datasource.local.dao.OrderDao
import com.tokopedia.sellerapp.data.datasource.local.model.OrderModel
import com.tokopedia.sellerapp.data.datasource.remote.OrderListModel
import com.tokopedia.sellerapp.data.mapper.OrderMapper.mapModelToOrderEntity
import com.tokopedia.sellerapp.data.mapper.OrderMapper.mapModelToProductEntity
import kotlinx.coroutines.flow.Flow

class OrderRoomDatasource(
    private val orderDao: OrderDao
) {
    fun saveOrderList(orderList: OrderListModel) {
        orderDao.insertOrderProducts(orderList.mapModelToProductEntity())
        orderDao.insertOrderList(orderList.mapModelToOrderEntity())
    }

    fun getOrderList() : Flow<List<OrderModel>>{
        return orderDao.getOrderList()
    }
}