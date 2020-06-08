package com.tokopedia.seller.util

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object EditShippingConstant {
    val BASE_MOBILE_URL = getInstance().MOBILEWEB
    val URL_TOKOPEDIA_CORNER = BASE_MOBILE_URL + "tokopedia-corner"
    val URL_BEBAS_ONGKIR = BASE_MOBILE_URL + "bebas-ongkir"
    val APPLINK_TOKOPEDIA_CORNER = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TOKOPEDIA_CORNER)
    val APPLINK_BEBAS_ONGKIR  = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_BEBAS_ONGKIR)
}