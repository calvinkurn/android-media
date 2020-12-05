package com.tokopedia.seller.product.draft.analytic

import com.tokopedia.track.TrackApp
import javax.inject.Inject

class ProductDraftListTracker @Inject constructor() {

    companion object {
        const val PAGE_SOURCE = "pageSource"

        // screen names
        const val SCREEN_NAME_ADD_PRODUCT = "/add-product"
    }

    fun sendScreen(screenName: String, pageSource: String) {
        val customDimensions = mapOf(
                PAGE_SOURCE to pageSource
        )
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimensions)
    }

    fun sendEventDraftProductClicked(label: String) {
        sendGeneralEvent(CLICK_DRAFT_PRODUCT, DRAFT_PRODUCT, CLICK, label)
    }

    private fun sendGeneralEvent(event: String, eventCategory: String, eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(event, eventCategory, eventAction, eventLabel)
    }
}