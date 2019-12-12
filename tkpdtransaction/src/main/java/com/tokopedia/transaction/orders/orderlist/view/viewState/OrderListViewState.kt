package com.tokopedia.transaction.orders.orderlist.view.viewState

import com.tokopedia.transaction.orders.orderlist.data.ActionButton
import com.tokopedia.transaction.orders.orderlist.data.Color
import com.tokopedia.transaction.orders.orderlist.data.MetaData

sealed class OrderListViewState
data class DotMenuVisibility(val visibility: Int) : OrderListViewState()
data class SetActionButtonData(val leftActionButton: ActionButton?, val rightActionButton: ActionButton?, val leftVisibility: Int, val rightVisibility: Int) : OrderListViewState()
data class SetCategoryAndTitle(val categoryName: String, val title: String): OrderListViewState()
data class SetItemCount(val itemCount: Int): OrderListViewState()
data class SetTotal(val totalLabel: String, val totalValue: String, val textColor: String): OrderListViewState()
data class SetDate(val date: String?): OrderListViewState()
data class SetInvoice(val invoice: String): OrderListViewState()
data class SetConditionalInfo(val successCondInfoVisibility: Int, val successConditionalText: String?, val color: Color?): OrderListViewState()
data class SetConditionalInfoBottom(val successCondInfoVisibility: Int, val successConditionalText: String?, val color: Color?): OrderListViewState()
data class SetFailStatusBgColor(val statusColor: String): OrderListViewState()
data class SetStatus(val statusText: String): OrderListViewState()
data class SetMetaDataToCustomView(val metaData: MetaData): OrderListViewState()
data class SetPaymentAvatar(val imgUrl: String): OrderListViewState()
