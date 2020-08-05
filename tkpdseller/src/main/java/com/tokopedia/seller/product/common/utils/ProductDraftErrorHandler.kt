package com.tokopedia.seller.product.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object ProductDraftErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}