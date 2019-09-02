package com.tokopedia.transaction.common.dialog

import android.app.Activity
import com.tokopedia.transaction.common.R

class SuccessTicketDialog(activity: Activity, page: Page) : UnifyDialog(activity, SINGLE_ACTION, NO_HEADER) {

    init {
        setTitle(activity.getString(R.string.success_ticket_dialog_title))
        setDescription(activity.getString(R.string.success_ticket_dialog_description))
        val okRes = when (page) {
            Page.PAGE_ATC -> R.string.success_ticket_dialog_atc_action
            Page.PAGE_CHECKOUT -> R.string.success_ticket_dialog_checkout_action
        }
        setOk(activity.getString(okRes))
    }

    enum class Page {
        PAGE_ATC, PAGE_CHECKOUT
    }
}