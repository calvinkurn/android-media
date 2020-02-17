package com.tokopedia.transaction.orders.orderlist.view.adapter.factory

import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.*

interface OrderListTypeFactory {

    fun type(viewModel: OrderListRecomViewModel): Int

    fun type(viewModel: EmptyStateMarketplaceViewModel): Int

    fun type(viewModel: EmptyStateOrderListViewModel): Int

    fun type(viewModel: EmptyStateMarketPlaceFilterViewModel): Int

    fun type(viewModel: OrderListRecomTitleViewModel): Int

    fun type(viewModel: OrderListViewModel): Int
}
