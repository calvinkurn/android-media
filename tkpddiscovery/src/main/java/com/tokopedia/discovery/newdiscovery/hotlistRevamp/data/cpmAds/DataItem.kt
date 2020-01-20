package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class DataItem(

        @field:SerializedName("redirect")
        val redirect: String? = null,

        @field:SerializedName("sticker_id")
        val stickerId: String? = null,

        @field:SerializedName("sticker_image")
        val stickerImage: String? = null,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("ad_click_url")
        val adClickUrl: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("headline")
        val headline: Headline? = null,

        @field:SerializedName("ad_ref_key")
        val adRefKey: String? = null
)