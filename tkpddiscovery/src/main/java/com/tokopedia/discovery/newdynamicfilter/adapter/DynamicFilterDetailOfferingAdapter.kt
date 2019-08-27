package com.tokopedia.discovery.newdynamicfilter.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView

import com.tokopedia.discovery.R
import com.tokopedia.discovery.common.data.Option
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView

class DynamicFilterDetailOfferingAdapter(filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailAdapter(filterDetailView) {

    override fun getLayout(): Int {
        return R.layout.filter_detail_offering
    }

    override fun getViewHolder(view: View): DynamicFilterDetailViewHolder {
        return OfferingItemViewHolder(view, filterDetailView)
    }

    private class OfferingItemViewHolder(itemView: View, filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailViewHolder(itemView, filterDetailView) {

        private val title: TextView
        private val description: TextView
        private val iconNew: View

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            iconNew = itemView.findViewById(R.id.icon_new)
        }

        override fun bind(option: Option) {
            super.bind(option)
            title.text = option.name
            description.text = option.description
            description.visibility = if (!TextUtils.isEmpty(option.description)) View.VISIBLE else View.GONE
            iconNew.visibility = if (option.isNew) View.VISIBLE else View.GONE
        }
    }
}
