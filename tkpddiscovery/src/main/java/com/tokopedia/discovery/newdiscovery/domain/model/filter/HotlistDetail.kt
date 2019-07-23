package com.tokopedia.discovery.newdiscovery.domain.model.filter

import com.google.gson.annotations.SerializedName

data class HotlistDetail(

	@field:SerializedName("queries")
	val queries: List<QueriesItem?>? = null
)