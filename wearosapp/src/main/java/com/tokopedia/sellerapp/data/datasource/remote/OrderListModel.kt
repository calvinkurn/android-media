package com.tokopedia.sellerapp.data.datasource.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderListModel(
    @SerializedName("orderList")
    @Expose
    val orderList: OrderList = OrderList()
) {

    data class OrderList(
        @SerializedName("list")
        @Expose
        val list: kotlin.collections.List<List> = listOf()
    )

    data class List(
        @SerializedName("order_id")
        @Expose
        val orderId: String = "",

        @SerializedName("order_status_id")
        @Expose
        val orderStatusId: String = "",

        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("order_total_price")
        @Expose
        val orderTotalPrice: String = "",

        @SerializedName("order_date")
        @Expose
        val orderDate: String = "",

        @SerializedName("deadline_text")
        @Expose
        val deadLineText: String = "",

        @SerializedName("courier_name")
        @Expose
        val courierName: String = "",

        @SerializedName("courier_product_name")
        @Expose
        val courierType: String = "",

        @SerializedName("destination_province")
        @Expose
        val destinationProvince: String = "",

        @SerializedName("order_product")
        @Expose
        val products: kotlin.collections.List<Product> = listOf(),
    )

    data class Product(
        @SerializedName("product_id")
        @Expose
        val productId: String = "",

        @SerializedName("product_name")
        @Expose
        val productName: String = "",

        @SerializedName("product_qty")
        @Expose
        val productQty: Int = 0,

        @SerializedName("picture")
        @Expose
        val picture: String = "",

        @SerializedName("order_note")
        @Expose
        val orderNote: String = "",
    )
}