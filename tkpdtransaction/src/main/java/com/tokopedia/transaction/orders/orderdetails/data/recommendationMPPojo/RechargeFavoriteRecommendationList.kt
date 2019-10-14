package com.tokopedia.transaction.orders.orderdetails.data.recommendationPojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class RechargeFavoriteRecommendationList(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)