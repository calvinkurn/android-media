package com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.OrderListTypeFactory

class OrderListRecomViewModel(val recommendationItem: RecommendationItem, val recomTitle: String) : Visitable<OrderListTypeFactory> {

    override fun type(typeFactory: OrderListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
