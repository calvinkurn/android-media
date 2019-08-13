package com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel

import android.arch.lifecycle.MutableLiveData
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

    private var position: Int = 0
    private val WAITING_THIRD_PARTY = 103
    private val WAITING_TRANSFER = 107
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
        if (order.actionButtons().size == 2) {
            val leftActionButton = order.actionButtons()[0]
            val rightActionButton = order.actionButtons()[1]
            orderListLiveData.value = SetActionButtonData(leftActionButton, rightActionButton, View.VISIBLE, View.VISIBLE)
        } else if (order.actionButtons().size == 1) {
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

        if (order.conditionalInfo().text() != "") {
            val conditionalInfo = order.conditionalInfo()
            orderListLiveData.value = SetConditionalInfo(View.VISIBLE, conditionalInfo.text(), conditionalInfo.color())
        } else {
            orderListLiveData.value = SetConditionalInfo(View.GONE, null, null)
        }
        orderListLiveData.value = SetInvoice(order.invoiceRefNum())
        var date: String? = order.createdAt()
        if (date != null && date.contains("T")) {
            date = lastUpdatedDate(order.createdAt())
        }
        orderListLiveData.value = SetDate(date)

        orderListLiveData.value = SetCategoryAndTitle(order.categoryName(), order.title())
        if (!order.itemCount.equals("0", ignoreCase = true) && !order.itemCount.equals("1", ignoreCase = true)) {
            orderListLiveData.value = SetItemCount(Integer.parseInt(order.itemCount) - 1)
        } else {
            orderListLiveData.value = SetItemCount(-1)
        }
        val metaDataList = order.metaData()
        for (metaData in metaDataList) {
            if (order.status() == WAITING_THIRD_PARTY || order.status() == WAITING_TRANSFER && (metaData.label().equals("Metode Pembayaran", ignoreCase = true) || metaData.label().equals("Kode Pembayaran", ignoreCase = true)))
                continue
            orderListLiveData.value = SetMetaDataToCustomView(metaData)
        }
        orderListLiveData.value = SetPaymentAvatar(order.paymentData().imageUrl())
        orderListLiveData.value = SetTotal(order.paymentData().label(), order.paymentData().value(), order.paymentData().textColor())
    }


    fun lastUpdatedDate(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
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
