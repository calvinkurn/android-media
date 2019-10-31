package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import androidx.annotation.LayoutRes
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
    private val heading = itemView?.findViewById<TextView>(R.id.empty_state_marketplace_heading)
    private val subText = itemView?.findViewById<TextView>(R.id.empty_state_marketplace_sub_text)

    override fun bind(element: EmptyStateMarketplaceViewModel?) {
        tryAgain?.hide()
        heading?.text = getString(R.string.tkpdtransaction_lets_hunt_fav_stuff)
        subText?.text = getString(R.string.tkpdtransaction_buy_dream_items)
    }
}