package com.tokopedia.seller.product.common.utils

import com.crashlytics.android.Crashlytics

object ProductDraftErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}