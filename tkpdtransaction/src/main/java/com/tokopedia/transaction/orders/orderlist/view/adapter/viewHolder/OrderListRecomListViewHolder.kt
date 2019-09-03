package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel

class OrderListRecomListViewHolder(itemView: View?, var orderListAnalytics: OrderListAnalytics, val actionListener: ActionListener?) : AbstractViewHolder<OrderListRecomViewModel>(itemView), RecommendationCardView.TrackingListener {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.bom_item_recomnedation
    }
    private var recommendationCardView = itemView?.findViewById<RecommendationCardView>(R.id.bomRecommendationCardView)

    override fun bind(element: OrderListRecomViewModel) {
        recommendationCardView?.setRecommendationModel(element.recommendationItem, this)
        recommendationCardView?.showAddToCartButton()
        recommendationCardView?.setAddToCartClickListener{
            actionListener?.onCartClicked(element)
        }

    }

    override fun onImpressionTopAds(item: RecommendationItem) {
        orderListAnalytics.eventRecommendationProductImpression(item, item.position)
    }

    override fun onImpressionOrganic(item: RecommendationItem) {
        orderListAnalytics.eventRecommendationProductImpression(item, item.position)
    }

    override fun onClickTopAds(item: RecommendationItem) {
        orderListAnalytics.eventEmptyBOMRecommendationProductClick(item, item.position)
    }

    override fun onClickOrganic(item: RecommendationItem) {
        orderListAnalytics.eventEmptyBOMRecommendationProductClick(item, item.position)
    }

    interface ActionListener{
        fun onCartClicked(productModel: Any)
    }
}