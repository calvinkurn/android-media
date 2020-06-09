package com.tokopedia.seller.shopsettings.shipping.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object EditShippingAnalytics {

    private const val CLICK_TEXT_DISINI = "clickTextDisini"
    private const val BO_SHIPPING_EDITOR = "bo shipping editor"
    private const val CLICK_DISINI = "click disini"

    private fun sendEventCategoryAction(event: String, eventCategory: String,
                                        eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                             eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }

    fun eventClickonTickerShippingEditor() {
        sendEventCategoryAction(CLICK_TEXT_DISINI, BO_SHIPPING_EDITOR, CLICK_DISINI)
    }
}