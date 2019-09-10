package com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder

import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.text.Html
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import com.tkpd.library.utils.ImageHandler
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.UnifiedOrderListRouter
import com.tokopedia.transaction.orders.common.view.DoubleTextView
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants
import com.tokopedia.transaction.orders.orderlist.data.*
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListViewModel
import com.tokopedia.transaction.orders.orderlist.view.viewState.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class OrderListViewHolder(itemView: View?, var orderListAnalytics: OrderListAnalytics,  var menuListener: OnMenuItemListener?) : AbstractViewHolder<OrderListViewModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.order_list

        private const val KEY_URI = "tokopedia"
        private const val KEY_URI_PARAMETER = "idem_potency_key"
        private const val KEY_URI_PARAMETER_EQUAL = "idem_potency_key="
        private const val KEY_FROM_PAYMENT = "?from_payment=false"
        private const val KEY_META_DATA = "a/n"
    }

    var status = itemView?.findViewById<TextView>(R.id.list_element_status)
    var date = itemView?.findViewById<TextView>(R.id.date)
    var invoice = itemView?.findViewById<TextView>(R.id.invoice)
    var orderListBtnOverflow = itemView?.findViewById<LinearLayout>(R.id.order_list_but_overflow)
    var conditionalInfoLayout = itemView?.findViewById<LinearLayout>(R.id.conditional_info_layout)
    var conditionalInfoText = itemView?.findViewById<TextView>(R.id.conditional_info)
    var imgShopAvatar = itemView?.findViewById<ImageView>(R.id.shop_avatar)
    var categoryName = itemView?.findViewById<TextView>(R.id.category_name)
    var title = itemView?.findViewById<TextView>(R.id.title)
    var itemCount = itemView?.findViewById<TextView>(R.id.itemCount)
    var paymentAvatar = itemView?.findViewById<ImageView>(R.id.status_shop_avatar)
    var totalLabel = itemView?.findViewById<TextView>(R.id.total_price_label)
    var total = itemView?.findViewById<TextView>(R.id.total)
    var leftButton = itemView?.findViewById<TextView>(R.id.left_button)
    var rightButton = itemView?.findViewById<TextView>(R.id.right_button)
    var parentMetadataLayout = itemView?.findViewById<LinearLayout>(R.id.metadata)

    var cornerRadiusValue: Float = 9f
    var topTextSize: Float = 11f
    var textSize: Float = 10f
    var padding16: Int = 16
    var orderCategory: String = ""
    var appLink: String = ""

    override fun bind(element: OrderListViewModel) {
        setObservers(element)
        orderCategory = element.order.category()
        appLink = element.order.appLink
        if (!(orderCategory == OrderCategory.DIGITAL || orderCategory == OrderCategory.FLIGHTS)) {
            appLink += KEY_FROM_PAYMENT
        }
        parentMetadataLayout?.removeAllViews()
        element.setViewData()
        element.setActionButtonData()
        element.setDotMenuVisibility()
        ImageHandler.loadImageThumbs(itemView.context, imgShopAvatar, element.order.items()[0].imageUrl())
        setClickListeners(element.order)

    }

    private fun setObservers(element: OrderListViewModel) {
        element.orderListLiveData.removeObservers(itemView.context as FragmentActivity)
        element.orderListLiveData.observe(itemView.context as FragmentActivity, Observer {
            when(it){
                is DotMenuVisibility -> {
                    orderListBtnOverflow?.visibility = it.visibility
                }
                is SetActionButtonData -> {
                    setButtonData(leftButton, it.leftVisibility, it.leftActionButton)
                    setButtonData(rightButton, it.rightVisibility, it.rightActionButton)
                }
                is SetCategoryAndTitle ->{
                    setCategoryAndTitle(it.title, it.categoryName)
                }
                is SetItemCount ->{
                    setItemCount(it.itemCount)
                }
                is SetTotal ->{
                    setTotal(it.textColor, it.totalLabel, it.totalValue)
                }
                is SetDate ->{
                    date?.text = it.date
                }
                is SetInvoice ->{
                    invoice?.text = it.invoice
                }
                is SetConditionalInfo ->{
                    setConditionalInfo(it.successConditionalText, it.successCondInfoVisibility, it.color)
                }
                is SetFailStatusBgColor ->{
                    status?.setBackgroundColor(android.graphics.Color.parseColor(it.statusColor))
                }
                is SetStatus ->{
                    status?.text = it.statusText
                }
                is SetMetaDataToCustomView ->{
                    setMetadata(it.metaData)
                }
                is SetPaymentAvatar ->{
                    setPaymentAvatar(it.imgUrl)
                }
            }
        })
    }

    private fun setCategoryAndTitle(titleText: String, category: String) {
        if (TextUtils.isEmpty(category))
            categoryName?.hide()
        else
            categoryName?.text = Html.fromHtml(Html.fromHtml(category).toString())
        title?.text = titleText
    }

    private fun setItemCount(count: Int) {
        if ((orderCategory.equals(OrderListContants.BELANJA, ignoreCase = true) || orderCategory.equals(OrderListContants.MARKETPLACE, ignoreCase = true)) && count > 0) {
            itemCount?.show()
            title?.hide()
            itemCount?.text = String.format(itemView.context.resources.getString(R.string.item_count), count)
        } else {
            itemCount?.hide()
        }
    }

    private fun setTotal(textColor: String, totalLabelText: String, totalValue: String) {
        totalLabel?.text = totalLabelText
        total?.text = totalValue
        if (textColor.isNotEmpty()) {
            total?.setTextColor(android.graphics.Color.parseColor(textColor))
        }
    }

    private fun setPaymentAvatar(imgUrl: String) {
        if (!TextUtils.isEmpty(imgUrl)) {
            ImageHandler.loadImageThumbs(itemView.context, paymentAvatar, imgUrl)
            paymentAvatar?.show()
        } else {
            paymentAvatar?.invisible()
        }
    }

    private fun setConditionalInfo(successConditionalText: String?, successCondInfoVisibility: Int, color: Color?) {
        if (successConditionalText!= null) {
            conditionalInfoLayout?.visibility = successCondInfoVisibility
            val shape = GradientDrawable()
            shape.apply {
                this.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = cornerRadiusValue
                setColor(android.graphics.Color.parseColor(color?.background()))
                setStroke(1, android.graphics.Color.parseColor(color?.border()))
            }
            conditionalInfoText?.background = shape
            conditionalInfoText?.setPadding(padding16, padding16, padding16, padding16)
            conditionalInfoText?.text = successConditionalText
        } else {
            conditionalInfoLayout?.hide()
        }
    }

    private fun setMetadata(metaData: MetaData) {
        val childLayout = DoubleTextView(itemView.context, LinearLayout.VERTICAL)
        childLayout.setTopText(metaData.label())
        childLayout.setTopTextSize(topTextSize)
        val value : String? = metaData.value()
        val tv = TextView(itemView.context)
        if (value?.contains(KEY_META_DATA) == true) {
            val values = value.split(KEY_META_DATA.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            tv.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            tv.textSize = textSize
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.text = KEY_META_DATA.plus(values[1])
            childLayout.setBottomText(values[0])
            parentMetadataLayout?.addView(childLayout)
            parentMetadataLayout?.addView(tv)
        } else {
            childLayout.setBottomText(value)
            parentMetadataLayout?.addView(childLayout)
        }
    }

    private fun setClickListeners(order: Order) {
        orderListBtnOverflow?.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            popup.menu.add(Menu.NONE, R.id.action_bantuan, Menu.NONE, "Bantuan")
            popup.menu.add(Menu.NONE, R.id.action_order_detail, Menu.NONE, "Lihat Order Detail")
            popup.setOnMenuItemClickListener(OnMenuPopupClicked(it.context, order))
            popup.show()
        }
        itemView.setOnClickListener {
            if (!TextUtils.isEmpty(appLink)) {
                orderListAnalytics.sendProductClickEvent(status?.text.toString())
                RouteManager.route(itemView.context, appLink)
            }
        }
    }

    private inner class OnMenuPopupClicked (private val context:Context, private val order: Order) : PopupMenu.OnMenuItemClickListener {

        private val list = order.dotMenu()
        private val URL_POSITION =0

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when {
                item.itemId == R.id.action_bantuan -> {
                    if (!list.isEmpty()&& !list.get(URL_POSITION).uri().isEmpty()){
                        menuListener?.startUri(list.get(URL_POSITION).uri())
                    }
                   true
                }
                item.itemId == R.id.action_order_detail -> {
                    if (!TextUtils.isEmpty(order.appLink)) {
                        RouteManager.route(context, order.appLink)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setButtonData(button: TextView?, visibility: Int, actionButton: ActionButton?) {
        button?.visibility = visibility
        if (TextUtils.isEmpty(actionButton?.label())) {
            button?.text = actionButton?.label()
            if (actionButton?.color() != null) {
                if (!TextUtils.isEmpty(actionButton.color().background())) {
                    button?.setBackgroundColor(android.graphics.Color.parseColor(actionButton.color().background()))
                }
                if (!TextUtils.isEmpty(actionButton.color().textColor())) {
                    button?.setTextColor(android.graphics.Color.parseColor(actionButton.color().textColor()))
                }
            }
            button?.setOnClickListener {
                var newUri = actionButton?.uri()
                if (newUri?.startsWith(KEY_URI) == true) {
                    if (newUri.contains(KEY_URI_PARAMETER)) {
                        val url = Uri.parse(newUri)
                        newUri = newUri.replace(url.getQueryParameter(KEY_URI_PARAMETER)!!, "")
                        newUri = newUri.replace(KEY_URI_PARAMETER_EQUAL, "")
                    }
                    RouteManager.route(it.context, newUri)
                } else if (!TextUtils.isEmpty(newUri)) {
                    try {
                        it.context.startActivity((it.context.applicationContext as UnifiedOrderListRouter).getWebviewActivityWithIntent(it.context,
                                URLEncoder.encode(newUri, "UTF-8")))
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    interface OnMenuItemListener {
        fun startUri(uri: String)
    }
}