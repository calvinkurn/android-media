package com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.RecommendationListTypeFactory

class OrderListRecomTitleViewModel(var title:String) : Visitable<RecommendationListTypeFactory> {
    override fun type(typeFactory: RecommendationListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
