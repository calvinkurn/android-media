package com.tokopedia.transaction.orders.orderdetails.data.recommendationPojo

import com.google.gson.annotations.SerializedName

data class RechargeWidgetResponse(

	@SerializedName("home_widget")
	val homeWidget: HomeWidget?
)
