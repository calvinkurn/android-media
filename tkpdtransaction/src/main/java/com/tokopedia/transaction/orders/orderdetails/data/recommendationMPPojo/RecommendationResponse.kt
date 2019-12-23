package com.tokopedia.transaction.orders.orderdetails.data.recommendationPojo

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("rechargeFavoriteRecommendationList")
	val rechargeFavoriteRecommendationList: RechargeFavoriteRecommendationList?
)