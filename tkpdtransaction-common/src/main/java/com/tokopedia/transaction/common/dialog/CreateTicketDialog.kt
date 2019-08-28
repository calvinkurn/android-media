package com.tokopedia.transaction.common.dialog

import android.app.Activity
import com.tokopedia.transaction.common.R

class CreateTicketDialog(activity: Activity, page: Page): UnifyDialog(activity, HORIZONTAL_ACTION, NO_HEADER) {

    init {
        val titleRes = when (page) {
            Page.PAGE_ATC -> R.string.create_ticket_dialog_atc_title
            Page.PAGE_CHECKOUT -> R.string.create_ticket_dialog_checkout_title
        }
        setTitle(activity.getString(titleRes))
        setOk(activity.getString(R.string.create_ticket_dialog_do_action))
        setSecondary(activity.getString(R.string.create_ticket_dialog_cancel_action))
    }

    companion object {

        enum class Page {
            PAGE_ATC, PAGE_CHECKOUT
        }
    }

}