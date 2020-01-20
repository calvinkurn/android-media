package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail

import com.google.gson.annotations.SerializedName

data class HotlistDetail(

        @field:SerializedName("fileName")
        val fileName: String? = null,

        @field:SerializedName("aliasKey")
        val aliasKey: String? = null,

        @field:SerializedName("filterShop")
        val filterShop: Int? = 0,

        @field:SerializedName("shareFilePath")
        val shareFilePath: String? = null,

        @field:SerializedName("pageTitle")
        val pageTitle: String? = null,

        @field:SerializedName("catalog")
        val catalog: String? = null,

        @field:SerializedName("strFilterAttribute")
        val strFilterAttribute: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("isTopads")
        var isTopads: Boolean = false,

        @field:SerializedName("cityID")
        val cityID: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("metaDescription")
        val metaDescription: String? = null,

        @field:SerializedName("isPromo")
        val isPromo: Boolean? = false,

        @field:SerializedName("queries")
        val queries: List<QueriesItem?>? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("metaRobot")
        val metaRobot: String? = null,

        @field:SerializedName("createTime")
        val createTime: String? = null,

        @field:SerializedName("filterAttribute")
        val filterAttribute: FilterAttribute? = null,

        @field:SerializedName("coverImage")
        val coverImage: String? = null,

        @field:SerializedName("metaTitle")
        val metaTitle: String? = null,

        @field:SerializedName("id")
        val id: Int? = 0,

        @field:SerializedName("productUrl")
        val productUrl: String? = null,

        @field:SerializedName("longDesc")
        val longDesc: List<Any?>? = null
)