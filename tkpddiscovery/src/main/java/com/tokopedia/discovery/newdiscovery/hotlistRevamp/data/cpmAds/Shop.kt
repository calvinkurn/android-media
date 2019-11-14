package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class Shop(

        @field:SerializedName("product")
        val product: List<ProductItem?>? = null,

        @field:SerializedName("gold_shop")
        val goldShop: Boolean? = false,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("domain")
        val domain: String? = null,

        @field:SerializedName("image_shop")
        val imageShop: ImageShop? = null,

        @field:SerializedName("gold_shop_badge")
        val goldShopBadge: Boolean? = false,

        @field:SerializedName("id")
        val id: String? = null
)