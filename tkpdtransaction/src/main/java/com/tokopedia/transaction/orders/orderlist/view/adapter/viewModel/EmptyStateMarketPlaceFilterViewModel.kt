package com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.OrderListTypeFactory

class EmptyStateMarketPlaceFilterViewModel: Visitable<OrderListTypeFactory> {
    override fun type(typeFactory: OrderListTypeFactory): Int {
        return typeFactory.type(this)
    }
}