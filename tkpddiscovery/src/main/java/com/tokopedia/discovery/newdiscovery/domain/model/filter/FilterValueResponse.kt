package com.tokopedia.discovery.newdiscovery.domain.model.filter

import com.google.gson.annotations.SerializedName

data class FilterValueResponse(

	@field:SerializedName("data")
	val data: Data? = null
)