package com.tokopedia.transaction.orders.orderdetails.data.recommendationPojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class RecommendationsItem(

	@field:SerializedName("productId")
	val productId: Int?,

	@field:SerializedName("webLink")
	val webLink: String?,

	@field:SerializedName("description")
	val description: String?,

	@field:SerializedName("clientNumber")
	val clientNumber: String?,

	@field:SerializedName("title")
	val title: String?,

	@field:SerializedName("type")
	val type: String?,

	@field:SerializedName("categoryName")
	val categoryName: String?,

	@field:SerializedName("operatorName")
	val operatorName: String?,

	@field:SerializedName("productName")
	val productName: String?,

	@field:SerializedName("appLink")
	val appLink: String?,

	@field:SerializedName("tagType")
	val tagType: Int?,

	@field:SerializedName("isATC")
	val isATC: Boolean?,

	@field:SerializedName("iconUrl")
	val iconUrl: String?,

	@field:SerializedName("position")
	val position: Int?,

	@field:SerializedName("tag")
	val tag: String?,

	@field:SerializedName("operatorID")
	val operatorID: Int?,

	@field:SerializedName("categoryId")
	val categoryId: Int?,

	@field:SerializedName("productPrice")
	val productPrice: Int?
)