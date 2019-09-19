package com.tokopedia.transaction.orders.orderlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.OrderListAdapterFactory
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateMarketplaceViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateOrderListViewModel

class OrderListAdapter(orderListAdapterFactory: OrderListAdapterFactory)
    : BaseAdapter<OrderListAdapterFactory>(orderListAdapterFactory) {

    private val emptyStateMarketPlaceViewModel = EmptyStateMarketplaceViewModel()
    private val emptyStateOrderListViewModel = EmptyStateOrderListViewModel()

    fun setEmptyMarketplace() {
        this.visitables.clear()
        addElement(emptyStateMarketPlaceViewModel)
    }

    fun setEmptyOrderList() {
        this.visitables.clear()
        addElement(emptyStateOrderListViewModel)
    }

}