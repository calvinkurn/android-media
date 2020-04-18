package com.tokopedia.seller.product.draft.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking
import com.tokopedia.product.addedit.tracking.sendGeneralEventCustom

object AddEditDraftTracking {

    private const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    private const val DRAFT_PRODUCT_PAGE = "draft product page"
    const val CLICK_ADD_PRODUCT = "click add product"
    const val CLICK_ADD_PRODUCT_WITHOUT_DRAFT = "click add product without draft"

    fun eventAddEditDraftClicked(shopId: String, action: String) {
        ProductAddEditTracking.getTracker().sendGeneralEventCustom(
                EVENT_CLICK_ADD_PRODUCT,
                DRAFT_PRODUCT_PAGE,
                action,
                "",
                mapOf("shopId" to shopId))
    }
}