package com.tokopedia.seller.purchase.detail.model.detail.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-10-18.
 */
data class OriginInfo (
    @SerializedName("origin_address")
    @Expose
    val originAddress: String = ""
)