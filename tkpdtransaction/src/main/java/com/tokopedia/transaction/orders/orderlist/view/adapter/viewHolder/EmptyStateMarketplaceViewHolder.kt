package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateMarketplaceViewModel

class EmptyStateMarketplaceViewHolder(itemView: View?) : AbstractViewHolder<EmptyStateMarketplaceViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list_empty_state_marketplace
    }
    private val tryAgain = itemView?.findViewById<TextView>(R.id.tryAgain)

    override fun bind(element: EmptyStateMarketplaceViewModel?) {
        tryAgain?.hide()
    }
}