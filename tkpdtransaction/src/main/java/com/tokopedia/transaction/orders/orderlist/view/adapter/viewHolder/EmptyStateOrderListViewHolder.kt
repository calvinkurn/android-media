package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.EmptyStateOrderListViewModel

class EmptyStateOrderListViewHolder(itemView: View?) : AbstractViewHolder<EmptyStateOrderListViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.empty_state_normal
    }

    override fun bind(element: EmptyStateOrderListViewModel?) {
    }

}