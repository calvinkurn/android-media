package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class Headline(

        @field:SerializedName("badges")
        val badges: List<BadgesItem?>? = null,

        @field:SerializedName("image")
        val image: Image? = null,

        @field:SerializedName("shop")
        val shop: Shop? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: String? = null
)