package com.tokopedia.transaction.orders.orderdetails.view.adapter

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderdetails.data.recommendationPojo.WidgetGridItem
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics

class RecommendationAdapter(val recommendationItems: List<WidgetGridItem>) : RecyclerView.Adapter<RecommendationAdapter.RechargeWidgetViewHolder>() {

    private val viewMap = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeWidgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_widget_itm_list, parent, false)
        return RechargeWidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: RechargeWidgetViewHolder, position: Int) {
        val item = recommendationItems.get(position)
        holder.renderTitle(item)
        holder.renderImage(item)
        holder.renderProduct(item)
        holder.renderSubtitle(item)
        holder.renderFooter(item)

        if (item.applink?.isNotEmpty() ?: false) {
            holder.itemView.setOnClickListener {
                RouteManager.route(holder.itemView.context, item.applink)
                OrderListAnalytics.eventWidgetClick(item, position)
            }
        }

    }


    override fun getItemCount(): Int {
        return recommendationItems.size
    }

    override fun onViewAttachedToWindow(holder: RechargeWidgetViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap[position]) {
            viewMap.put(position, true)
            OrderListAnalytics.eventWidgetListView(recommendationItems[position], position)
        }
    }

    class RechargeWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.title)
        var icon: AppCompatImageView = itemView.findViewById(R.id.icon)
        var productName: TextView = itemView.findViewById(R.id.productName)
        var subtitle: TextView = itemView.findViewById(R.id.subtitle)
        var footer: View = itemView.findViewById(R.id.footer)
        var tagLine: TextView = itemView.findViewById(R.id.tagLine)
        var pricePrefix: TextView = itemView.findViewById(R.id.pricePrefix)
        var strikeThroughPrice: TextView = itemView.findViewById(R.id.strikeThroughPrice)
        var price: TextView = itemView.findViewById(R.id.price)

        fun renderTitle(element: WidgetGridItem) {
            if (element.titleFirst.isNullOrEmpty()) {
                title.visibility = View.GONE
            } else {
                title.visibility = View.VISIBLE
                title.text = MethodChecker.fromHtml(element.titleFirst)
            }

            if (element.descFirst.isNullOrEmpty()) {
                if ((hasPrice(element) || hasTagLabel(element))) {
                    title.maxLines = 2
                } else {
                    title.maxLines = 3
                }
            } else {
                title.maxLines = 1
            }
        }

        fun renderImage(element: WidgetGridItem) {
            ImageHandler.loadImageThumbs(itemView.context, icon, element.imageUrl)

        }

        fun hasPrice(element: WidgetGridItem): Boolean {
            return element.price.isNullOrEmpty().not()
                    || element.pricePrefix.isNullOrEmpty().not()
                    || element.originalPrice.isNullOrEmpty().not()
        }

        fun hasTagLabel(element: WidgetGridItem): Boolean {
            return !element.tagName.isNullOrEmpty()
        }

        fun renderProduct(element: WidgetGridItem) {
            if (element.name.isNullOrEmpty()) {
                productName.visibility = View.GONE
            } else {
                productName.visibility = View.VISIBLE
                productName.text = MethodChecker.fromHtml(element.name)
            }
        }

        fun renderSubtitle(element: WidgetGridItem) {
            if (element.descFirst.isNullOrEmpty()) {
                subtitle.visibility = View.GONE
            } else {
                subtitle.visibility = View.VISIBLE
                subtitle.text = MethodChecker.fromHtml(element.descFirst)
            }

            if (element.titleFirst.isNullOrEmpty() &&
                    element.tagName.isNullOrEmpty()) {
                if (hasPrice(element) || hasTagLabel(element)) {
                    subtitle.maxLines = 2
                } else {
                    subtitle.maxLines = 3
                }
            } else {
                subtitle.maxLines = 1
            }
        }

        fun renderFooter(element: WidgetGridItem) {
            if (hasPrice(element) || hasTagLabel(element)) {
                footer.visibility = View.VISIBLE
                renderLabel(element)
                renderPrice(element)
            } else {
                footer.visibility = View.GONE
            }
        }


        fun renderLabel(element: WidgetGridItem) {
            if (hasTagLabel(element)) {
                tagLine.visibility = View.VISIBLE
                tagLine.text = MethodChecker.fromHtml(element.tagName)
                when (element.tagType) {
                    1 -> {
                        tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_pink_label)
                        tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_transaction_label_pink))
                    }
                    2 -> {
                        tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_green_label)
                        tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_transaction_label_green))
                    }
                    3 -> {
                        tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_blue_label)
                        tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_transaction_label_blue))
                    }
                    4 -> {
                        tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_yellow_label)
                        tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_transaction_label_yellow))
                    }
                    5 -> {
                        tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_grey_label)
                        tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_transaction_label_grey))
                    }
                    else -> {
                        tagLine.visibility = View.GONE
                    }
                }
            } else {
                tagLine.visibility = View.GONE
            }

        }

        fun renderPrice(element: WidgetGridItem) {
            if (hasPrice(element)) {

                if (element.pricePrefix.isNullOrEmpty()) {
                    pricePrefix.visibility = View.GONE
                } else {
                    pricePrefix.visibility = View.VISIBLE
                    pricePrefix.text = MethodChecker.fromHtml(element.pricePrefix)
                }

                if (element.originalPrice.isNullOrEmpty()) {
                    strikeThroughPrice.visibility = View.GONE
                } else {
                    strikeThroughPrice.visibility = View.VISIBLE
                    strikeThroughPrice.text = MethodChecker.fromHtml(element.originalPrice)
                    strikeThroughPrice.paintFlags = strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }

                if (element.price.isNullOrEmpty()) {
                    price.visibility = View.GONE
                } else {
                    price.visibility = View.VISIBLE
                    price.text = MethodChecker.fromHtml(element.price)
                }

            } else {
                price.visibility = View.GONE
                pricePrefix.visibility = View.GONE
                strikeThroughPrice.visibility = View.GONE
            }
        }


    }
}

