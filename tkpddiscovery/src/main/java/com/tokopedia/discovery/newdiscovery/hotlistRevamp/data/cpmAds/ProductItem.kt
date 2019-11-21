package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class ProductItem(

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("price_format")
        val priceFormat: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("image_product")
        val imageProduct: ImageProduct? = null,

        @field:SerializedName("id")
        val id: String? = null
)