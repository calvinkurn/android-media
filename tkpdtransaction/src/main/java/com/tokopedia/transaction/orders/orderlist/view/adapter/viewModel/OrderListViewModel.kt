package com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel

import androidx.lifecycle.MutableLiveData
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants
import com.tokopedia.transaction.orders.orderlist.data.Order
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.OrderListTypeFactory
import com.tokopedia.transaction.orders.orderlist.view.viewState.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OrderListViewModel(var order: Order) : Visitable<OrderListTypeFactory> {

    companion object {
        private const val WAITING_THIRD_PARTY = 103
        private const val WAITING_TRANSFER = 107
        private const val SINGLE_BUTTON_VIEW = 1
        private const val TWO_BUTTON_VIEW = 2
        private const val PAYMENT_METHOD = "Metode Pembayaran"
        private const val PAYMENT_CODE = "Kode Pembayaran"
        private const val DATE_WIB_T = "T"
        private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        private const val OUTPUT_DATE_FORMAT = "dd MMM yyyy"
    }

    var orderListLiveData: MutableLiveData<OrderListViewState> = MutableLiveData()

    override fun type(typeFactory: OrderListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun setDotMenuVisibility() {
        if (order.category().equals(OrderListContants.BELANJA, true) || order.category().equals(OrderListContants.MARKETPLACE, true)) {
            orderListLiveData.value = DotMenuVisibility(View.GONE)
        } else if (order.dotMenu() != null) {
            orderListLiveData.value = DotMenuVisibility(View.VISIBLE)
        }
    }

    fun setActionButtonData() {
        if (order.actionButtons().size == TWO_BUTTON_VIEW) {
            val leftActionButton = order.actionButtons()[0]
            val rightActionButton = order.actionButtons()[1]
            orderListLiveData.value = SetActionButtonData(leftActionButton, rightActionButton, View.VISIBLE, View.VISIBLE)
        } else if (order.actionButtons().size == SINGLE_BUTTON_VIEW) {
            val actionButton = order.actionButtons()[0]
            if (actionButton.buttonType() == "primary") {
                orderListLiveData.value = SetActionButtonData(actionButton, null, View.VISIBLE, View.GONE)
            } else {
                orderListLiveData.value = SetActionButtonData(null, actionButton, View.GONE, View.VISIBLE)
            }
        } else {
            orderListLiveData.value = SetActionButtonData(null, null, View.GONE, View.GONE)
        }
    }

    fun setViewData() {
        orderListLiveData.value = SetStatus(order.statusStr())
        orderListLiveData.value = SetFailStatusBgColor(order.statusColor())

        if (!TextUtils.isEmpty(order.conditionalInfo().text())) {
            val conditionalInfo = order.conditionalInfo()
            orderListLiveData.value = SetConditionalInfo(View.VISIBLE, conditionalInfo.text(), conditionalInfo.color())
        } else {
            orderListLiveData.value = SetConditionalInfo(View.GONE, null, null)
        }
        if (!TextUtils.isEmpty(order.conditionalInfoBottom().text())) {
            val conditionalInfoBottom = order.conditionalInfoBottom()
            orderListLiveData.value = SetConditionalInfoBottom(View.VISIBLE, conditionalInfoBottom.text(), conditionalInfoBottom.color())
        } else {
            orderListLiveData.value = SetConditionalInfoBottom(View.GONE, null, null)
        }
        orderListLiveData.value = SetInvoice(order.invoiceRefNum())
        var date: String? = order.createdAt()
        if (date?.contains(DATE_WIB_T) == true) {
            date = lastUpdatedDate(order.createdAt())
        }
        orderListLiveData.value = SetDate(date)
        orderListLiveData.value = SetCategoryAndTitle(order.categoryName(), order.title())
        orderListLiveData.value = SetItemCount(Integer.parseInt(order.itemCount) - 1)
        val metaDataList = order.metaData()
        for (metaData in metaDataList) {
            if (order.status() == WAITING_THIRD_PARTY || order.status() == WAITING_TRANSFER && (metaData.label().equals(PAYMENT_METHOD, ignoreCase = true) || metaData.label().equals(PAYMENT_CODE, ignoreCase = true)))
                continue
            orderListLiveData.value = SetMetaDataToCustomView(metaData)
        }
        orderListLiveData.value = SetPaymentAvatar(order.paymentData().imageUrl())
        orderListLiveData.value = SetTotal(order.paymentData().label(), order.paymentData().value(), order.paymentData().textColor())
    }


    private fun lastUpdatedDate(date: String): String {
        val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        val output = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.getDefault())
        var formattedTime = ""
        try {
            val d = sdf.parse(date)
            formattedTime = output.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedTime
    }
}
