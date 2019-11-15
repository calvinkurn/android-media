package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class ImageProduct(

        @field:SerializedName("image_url")
        val imageUrl: String? = null,

        @field:SerializedName("product_id")
        val productId: String? = null,

        @field:SerializedName("image_click_url")
        val imageClickUrl: String? = null,

        @field:SerializedName("product_name")
        val productName: String? = null
)