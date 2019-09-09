package com.tokopedia.transaction.orders.orderdetails.view.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.SizeSmallBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics

class RecommendationAdapter(val recommendationItems: List<HomeWidget.ContentItemTab>, private val businessUnitItemView: BusinessUnitItemView) : RecyclerView.Adapter<SizeSmallBusinessViewHolder>() {

    private val viewMap = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SizeSmallBusinessViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_template_small_business, parent, false)
        return SizeSmallBusinessViewHolder(view, businessUnitItemView)
    }

    override fun onBindViewHolder(holder: SizeSmallBusinessViewHolder, position: Int) {
        val item = recommendationItems.get(position)
        holder.renderTitle(item)
        holder.renderImage(item)
        holder.renderProduct(item)
        holder.renderTitle(item)
        holder.renderSubtitle(item)
        holder.renderFooter(item)
        holder.addImpressionListener(item)

        if (item.applink.isNotEmpty()) {
            holder.itemView.setOnClickListener {
                RouteManager.route(holder.itemView.context, item.applink)
                OrderListAnalytics.eventWidgetClick(item, position)
            }
        }

    }


    override fun getItemCount(): Int {
        return recommendationItems.size
    }

    override fun onViewAttachedToWindow(holder: SizeSmallBusinessViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap[position]) {
            viewMap.put(position,true)
            OrderListAnalytics.eventWidgetListView(recommendationItems[position], position)
        }
    }
}

