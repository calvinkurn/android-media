package com.tokopedia.seller.manageitem.common.util

import android.text.TextUtils
import com.crashlytics.android.Crashlytics
import com.tokopedia.network.data.model.response.ResponseV4ErrorException

/**
 * @author by milhamj on 2020-02-06.
 */

object UploadProductErrorHandler {
    @JvmStatic
    fun getExceptionMessage(t: Throwable): String {
        return if (t is ResponseV4ErrorException && !TextUtils.isEmpty(t.errorList.firstOrNull())) {
            t.errorList.firstOrNull() ?: t.localizedMessage
        } else {
            t.localizedMessage
        }
    }

    @JvmStatic
    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}