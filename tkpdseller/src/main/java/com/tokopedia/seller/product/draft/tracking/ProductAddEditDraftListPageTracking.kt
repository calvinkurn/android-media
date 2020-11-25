package com.tokopedia.seller.product.draft.tracking

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

object ProductAddEditDraftListPageTracking {
    var gtmTracker: ContextAnalytics? = null

    const val EXTRA_IS_EDIT_MODE = "EXTRA_IS_EDIT_MODE"
    const val CLICK_ADD_PRODUCT = "click add product"
    const val CLICK_ADD_PRODUCT_WITHOUT_DRAFT = "click add product without draft"
    private const val KEY_EVENT = "event"
    private const val KEY_CATEGORY = "eventCategory"
    private const val KEY_ACTION = "eventAction"
    private const val KEY_LABEL = "eventLabel"
    private const val KEY_SHOP_ID = "shopId"
    private const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    private const val EVENT_CLICK_EDIT_PRODUCT = "clickEditProduct"
    private const val CAT_DRAFT_PRODUCT_PAGE = "draft product page"
    private const val CAT_EDIT_PRODUCT_PAGE = "edit product page"
    private const val ACTION_BACK_CHOOSE_OTHER = "click back choose other categories"

    fun eventAddEditDraftClicked(shopId: String, action: String) {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_ADD_PRODUCT,
                CAT_DRAFT_PRODUCT_PAGE,
                action,
                "",
                mapOf(KEY_SHOP_ID to shopId)
        )
    }

    fun clickBackOtherCategory(shopId: String) {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_EDIT_PRODUCT,
                CAT_EDIT_PRODUCT_PAGE,
                ACTION_BACK_CHOOSE_OTHER,
                "",
                mapOf(KEY_SHOP_ID to shopId))
    }

    private fun getTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().getGTM()
        }
        return gtmTracker!!
    }

    private fun ContextAnalytics.sendGeneralEventCustom(event: String, category: String,
                                                action: String, label: String,
                                                customDimension: Map<String, String>) {
        sendGeneralEvent(createEventMap(event, category, action, label, customDimension))
    }

    private fun createEventMap(event: String, category: String,
                       action: String, label: String,
                       customDimension: Map<String, String>): Map<String, String> {
        val map = mutableMapOf(
                KEY_EVENT to event,
                KEY_CATEGORY to category,
                KEY_ACTION to action, KEY_LABEL to label)
        map.putAll(customDimension)
        return map
    }

}