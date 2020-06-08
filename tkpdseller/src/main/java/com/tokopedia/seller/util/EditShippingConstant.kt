package com.tokopedia.seller.util

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object EditShippingConstant {
    val BASE_MOBILE_URL = getInstance().MOBILEWEB
    val URL_TOKOPEDIA_CORNER = BASE_MOBILE_URL + "tokopedia-corner"
    val APPLINK_TOKOPEDIA_CORNER = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TOKOPEDIA_CORNER)
}