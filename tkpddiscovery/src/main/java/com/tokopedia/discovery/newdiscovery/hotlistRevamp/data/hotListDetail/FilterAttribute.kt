package com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail

import com.google.gson.annotations.SerializedName

data class FilterAttribute(

        @field:SerializedName("pmax")
        val pmax: Int? = 0,

        @field:SerializedName("rt")
        val rt: String? = null,

        @field:SerializedName("scheme")
        val scheme: String? = null,

        @field:SerializedName("full_domain")
        val fullDomain: String? = null,

        @field:SerializedName("hot_id")
        val hotId: Int? = 0,

        @field:SerializedName("etalase")
        val etalase: Int? = 0,

        @field:SerializedName("discount")
        val discount: Int? = 0,

        @field:SerializedName("official")
        val official: Boolean? = false,

        @field:SerializedName("fq")
        val fq: String? = null,

        @field:SerializedName("nuq")
        val nuq: String? = null,

        @field:SerializedName("source")
        val source: String? = null,

        @field:SerializedName("xsname")
        val xsname: String? = null,

        @field:SerializedName("image_square")
        val imageSquare: Boolean? = false,

        @field:SerializedName("sc_identifier")
        val scIdentifier: Int? = 0,

        @field:SerializedName("fshop")
        val fshop: Int? = 0,

        @field:SerializedName("sc")
        val sc: String? = null,

        @field:SerializedName("negative")
        val negative: String? = null,

        @field:SerializedName("ob")
        val ob: Int? = 0,

        @field:SerializedName("wholesale")
        val wholesale: Boolean? = false,

        @field:SerializedName("shipping")
        val shipping: String? = null,

        @field:SerializedName("min_sold")
        val minSold: Int? = 0,

        @field:SerializedName("pmin")
        val pmin: Int? = 0,

        @field:SerializedName("fcity")
        val fcity: String? = null,

        @field:SerializedName("kreasi_lokal")
        val kreasiLokal: Boolean? = false,

        @field:SerializedName("dname")
        val dname: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("negative_sc")
        val negativeSc: String? = null,

        @field:SerializedName("max_sold")
        val maxSold: Int? = 0,

        @field:SerializedName("st")
        val st: String? = null,

        @field:SerializedName("ctg_id")
        val ctgId: Int? = 0,

        @field:SerializedName("start")
        val start: Int? = 0,

        @field:SerializedName("catalog_rows")
        val catalogRows: Int? = 0,

        @field:SerializedName("floc")
        val floc: String? = null,

        @field:SerializedName("is_curated")
        val isCurated: Boolean? = false,

        @field:SerializedName("rows")
        val rows: Int? = 0,

        @field:SerializedName("cashback")
        val cashback: Boolean? = false,

        @field:SerializedName("q")
        val Q: String? = null,

        @field:SerializedName("shop_id")
        val shopId: String? = null,

        @field:SerializedName("condition")
        val condition: Int? = 0,

        @field:SerializedName("freereturns")
        val freereturns: Boolean? = false,

        @field:SerializedName("rf")
        val rf: Boolean? = false,

        @field:SerializedName("sname")
        val sname: String? = null,

        @field:SerializedName("xq")
        val xq: String? = null,

        @field:SerializedName("default_sc")
        val defaultSc: String? = null,

        @field:SerializedName("is_featured")
        val isFeatured: Boolean? = false,

        @field:SerializedName("preorder")
        val preorder: Boolean? = false,

        @field:SerializedName("cid")
        val cid: Int? = 0
)