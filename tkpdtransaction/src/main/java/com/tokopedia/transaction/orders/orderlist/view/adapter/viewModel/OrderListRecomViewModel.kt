package com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.RecommendationListTypeFactory

class OrderListRecomViewModel(val recommendationItem: RecommendationItem) : Visitable<RecommendationListTypeFactory> {

    override fun type(typeFactory: RecommendationListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
