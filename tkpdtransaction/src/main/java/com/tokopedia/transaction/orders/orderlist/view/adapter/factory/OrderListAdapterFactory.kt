package com.tokopedia.transaction.orders.orderlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.*
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.*

class OrderListAdapterFactory(var orderListAnalytics: OrderListAnalytics, var listener: OrderListViewHolder.OnMenuItemListener?,
                              var cartListener: OrderListRecomListViewHolder.ActionListener,
                              var filterListener: EmptyStateMarketPlaceFilterViewHolder.ActionListener,
                              var buttonListener: OrderListViewHolder.OnActionButtonListener?) : BaseAdapterTypeFactory(), OrderListTypeFactory {
    override fun type(viewModel: OrderListViewModel): Int {
        return OrderListViewHolder.LAYOUT
    }

    override fun type(viewModel: OrderListRecomTitleViewModel): Int {
        return OrderListRecomTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyStateOrderListViewModel): Int {
        return EmptyStateOrderListViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyStateMarketPlaceFilterViewModel): Int {
        return EmptyStateMarketPlaceFilterViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyStateMarketplaceViewModel): Int {
        return EmptyStateMarketplaceViewHolder.LAYOUT
    }

    override fun type(viewModel: OrderListRecomViewModel): Int {
        return OrderListRecomListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderListViewHolder.LAYOUT -> OrderListViewHolder(parent, orderListAnalytics, listener, buttonListener)
            OrderListRecomListViewHolder.LAYOUT -> OrderListRecomListViewHolder(parent, orderListAnalytics, cartListener)
            OrderListRecomTitleViewHolder.LAYOUT -> OrderListRecomTitleViewHolder(parent)
            EmptyStateMarketplaceViewHolder.LAYOUT -> EmptyStateMarketplaceViewHolder(parent)
            EmptyStateOrderListViewHolder.LAYOUT -> EmptyStateOrderListViewHolder(parent)
            EmptyStateMarketPlaceFilterViewHolder.LAYOUT -> EmptyStateMarketPlaceFilterViewHolder(parent,filterListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
