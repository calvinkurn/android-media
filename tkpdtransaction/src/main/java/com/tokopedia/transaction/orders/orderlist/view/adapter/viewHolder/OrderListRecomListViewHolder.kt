package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel

class OrderListRecomListViewHolder(itemView: View?, var orderListAnalytics: OrderListAnalytics) : AbstractViewHolder<OrderListRecomViewModel>(itemView), RecommendationCardView.TrackingListener {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.bom_item_recomnedation
    }
    private var recommendationCardView = itemView?.findViewById<RecommendationCardView>(R.id.bomRecommendationCardView)

    override fun bind(element: OrderListRecomViewModel) {
        recommendationCardView?.setRecommendationModel(element.recommendationItem, this)
        recommendationCardView?.showAddToCartButton()

    }

    override fun onImpressionTopAds(item: RecommendationItem) {

    }

    override fun onImpressionOrganic(item: RecommendationItem) {

    }

    override fun onClickTopAds(item: RecommendationItem) {

    }

    override fun onClickOrganic(item: RecommendationItem) {

    }
}