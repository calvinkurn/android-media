package com.tokopedia.seller.shopsettings.shipping.data

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object EditShippingConstant {
    val BASE_MOBILE_URL = getInstance().MOBILEWEB

    val URL_BEBAS_ONGKIR = BASE_MOBILE_URL + "bebas-ongkir"

    val APPLINK_BEBAS_ONGKIR  = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_BEBAS_ONGKIR)
}