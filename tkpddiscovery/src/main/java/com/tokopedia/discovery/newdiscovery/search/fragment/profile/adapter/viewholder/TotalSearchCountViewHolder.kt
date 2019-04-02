package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.TotalSearchCountViewModel
import kotlinx.android.synthetic.main.layout_search_count.view.*

class TotalSearchCountViewHolder(val view: View) : AbstractViewHolder<TotalSearchCountViewModel>(view) {
    override fun bind(model: TotalSearchCountViewModel) {
        view.tv_search_count.text =
                String.format(
                        view.context.getString(R.string.value_profile_count),
                        model.searchCountText
                )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_search_count
    }
}
