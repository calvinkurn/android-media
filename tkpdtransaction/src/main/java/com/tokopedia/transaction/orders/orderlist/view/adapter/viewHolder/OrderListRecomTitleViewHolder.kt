package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomTitleViewModel

class OrderListRecomTitleViewHolder(itemView: View?) : AbstractViewHolder<OrderListRecomTitleViewModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list_recom_title_item
    }
    private var title = itemView?.findViewById<TextView>(R.id.title)

    override fun bind(element: OrderListRecomTitleViewModel) {
        title?.text = element.title
    }
}