package com.tokopedia.session.addchangepassword.data.analytics

import com.tokopedia.track.TrackApp

class AddPasswordAnalytics {
    internal  val tracker = TrackApp.getInstance().gtm

    fun onClickSubmit() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK
        )
    }

    fun onSuccessAddPassword() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK_SUCCESS
        )
    }

    fun onFailedAddPassword(message: String) {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                "${LABEL.CLICK_FAILED} - $message"
        )
    }

    companion object {
        object EVENT {
            internal const val CLICK_LOGIN = "clickAccount"
        }

        object CATEGORY {
            internal const val ACCOUNT_SETTING = "account setting - password"
        }

        object ACTION {
            internal const val CLICK_ON_BUTTON_SUBMIT = "click on buat kata sandi"
        }

        object LABEL {
            internal const val CLICK = "click"
            internal const val CLICK_SUCCESS = "success"
            internal const val CLICK_FAILED = "failed"
        }
    }
}