package com.tokopedia.transaction.orders.orderlist.view.adapter.factory

import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateMarketplaceViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateOrderListViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomTitleViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel

interface RecommendationListTypeFactory {

    fun type(viewModel: OrderListRecomViewModel): Int

    fun type(viewModel: EmptyStateMarketplaceViewModel): Int

    fun type(viewModel: EmptyStateOrderListViewModel): Int

    fun type(viewModel: OrderListRecomTitleViewModel): Int
}
