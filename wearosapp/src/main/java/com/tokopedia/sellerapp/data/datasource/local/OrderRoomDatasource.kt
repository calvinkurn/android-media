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
        orderDao.clear(orderList.orderList.list.map { it.orderStatusId }.distinct().toTypedArray())
        orderDao.insertOrderList(orderList.mapModelToOrderEntity())
        orderDao.insertOrderProducts(orderList.mapModelToProductEntity())
    }

    fun getOrderList(orderStatus: Array<String>) : Flow<List<OrderWithProduct>>{
        return orderDao.getOrderList(orderStatus)
    }

    fun getOrderDetail(orderId: String) : Flow<OrderWithProduct>{
        return orderDao.getOrderDetail(orderId)
    }

    fun getOrderCount(orderStatus: Array<String>) : Flow<Int>{
        return orderDao.getOrderCount(orderStatus)
    }
}
