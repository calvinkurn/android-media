package com.tokopedia.seller.shopsettings.shipping.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

object EditShippingAnalytics {

    private const val CLICK_TEXT_DISINI = "clickBebasOngkir"
    private const val BO_SHIPPING_EDITOR = "bo shipping editor"
    private const val CLICK_DISINI = "click disini"

    private const val KEY_USER_ID = "userId"

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    @JvmStatic
    fun eventClickonTickerShippingEditor(userId: String) {
        val map = TrackAppUtils.gtmData(
                CLICK_TEXT_DISINI,
                BO_SHIPPING_EDITOR,
                CLICK_DISINI,
                "")
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

}