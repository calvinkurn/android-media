package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.transaction.orders.orderlist.view.adapter.WishListResponseListener
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel

class OrderListRecomListViewHolder(itemView: View?, var orderListAnalytics: OrderListAnalytics, val actionListener: ActionListener?) : AbstractViewHolder<OrderListRecomViewModel>(itemView), RecommendationCardView.TrackingListener, WishListResponseListener {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.bom_item_recomnedation
    }
    private var recommendationCardView = itemView?.findViewById<RecommendationCardView>(R.id.bomRecommendationCardView)
    private val wishlist = recommendationCardView?.findViewById<ImageView>(R.id.btn_wishlist)
    private var recomTitle : String = "none/other"
    private var isSelected: Boolean = false

    override fun bind(element: OrderListRecomViewModel) {
        recommendationCardView?.setRecommendationModel(element.recommendationItem, this)
        recommendationCardView?.showAddToCartButton()
        recommendationCardView?.setAddToCartClickListener{
            actionListener?.onCartClicked(element)
        }
        isSelected = element.recommendationItem.isWishlist
        wishlist?.show()
        setWishlistDrawable()
        wishlist?.setOnClickListener {
            actionListener?.onWishListClicked(element, !isSelected, this)
        }
        recomTitle = element.recomTitle
    }

    private fun setWishlistDrawable() {
        if(isSelected){
            wishlist?.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.product_card_ic_wishlist_red))
        } else {
            wishlist?.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.product_card_ic_wishlist))
        }
    }

    override fun onImpressionTopAds(item: RecommendationItem) {
        orderListAnalytics.eventRecommendationProductImpression(item, item.position, recomTitle)
    }

    override fun onImpressionOrganic(item: RecommendationItem) {
        orderListAnalytics.eventRecommendationProductImpression(item, item.position, recomTitle)
    }

    override fun onClickTopAds(item: RecommendationItem) {
        orderListAnalytics.eventEmptyBOMRecommendationProductClick(item, item.position, recomTitle)
    }

    override fun onClickOrganic(item: RecommendationItem) {
        orderListAnalytics.eventEmptyBOMRecommendationProductClick(item, item.position, recomTitle)
    }

    override fun onWhishListSuccessResponse(isSelected: Boolean) {
        this.isSelected = isSelected
        setWishlistDrawable()
        orderListAnalytics.sendWishListClickEvent(isSelected)
    }

    interface ActionListener{
        fun onCartClicked(productModel: Any)
        fun onWishListClicked(productModel: Any, isSelected: Boolean, onWishListResponseListener: WishListResponseListener)
    }
}