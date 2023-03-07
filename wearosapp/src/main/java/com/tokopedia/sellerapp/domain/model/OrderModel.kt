package com.tokopedia.sellerapp.domain.model

data class OrderModel (
    val orderId: String = "",
    val orderStatusId: String = "",
    val orderTotalPrice: String = "",
    val orderDate: String = "",
    val deadLineText: String = "",
    val courierName: String = "",
    val destinationProvince: String = "",
    val products: List<Product> = listOf(),
){
    data class Product(
        val productName: String = "",
        val productImage: String = "",
        val orderNote: String = "",
    )
}