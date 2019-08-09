package com.tokopedia.transaction.orders.orderlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.EmptyStateMarketplaceViewHolder
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.EmptyStateOrderListViewHolder
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.OrderListRecomListViewHolder
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.OrderListRecomTitleViewHolder
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateMarketplaceViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateOrderListViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomTitleViewModel
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel

class RecommendationListAdapterFactory(var orderListAnalytics: OrderListAnalytics) : BaseAdapterTypeFactory(), RecommendationListTypeFactory {
    override fun type(viewModel: OrderListRecomTitleViewModel): Int {
        return OrderListRecomTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyStateOrderListViewModel): Int {
        return EmptyStateOrderListViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyStateMarketplaceViewModel): Int {
        return EmptyStateMarketplaceViewHolder.LAYOUT
    }

    override fun type(viewModel: OrderListRecomViewModel): Int {
        return OrderListRecomListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderListRecomListViewHolder.LAYOUT -> OrderListRecomListViewHolder(parent, orderListAnalytics)
            OrderListRecomTitleViewHolder.LAYOUT -> OrderListRecomTitleViewHolder(parent)
            EmptyStateMarketplaceViewHolder.LAYOUT -> EmptyStateMarketplaceViewHolder(parent)
            EmptyStateOrderListViewHolder.LAYOUT -> EmptyStateOrderListViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
