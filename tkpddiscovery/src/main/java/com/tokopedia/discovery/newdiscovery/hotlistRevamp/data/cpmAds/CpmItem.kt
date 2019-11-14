package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class CpmItem(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("price_format")
        val price_format: String? = null,

        @field:SerializedName("is_product")
        val is_product: Boolean = false,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("click_url")
        val click_url: String? = null,

        @field:SerializedName("badge_url")
        val badge_url: String? = null


)