package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class Image(

        @field:SerializedName("full_url")
        val fullUrl: String? = null,

        @field:SerializedName("full_ecs")
        val fullEcs: String? = null
)