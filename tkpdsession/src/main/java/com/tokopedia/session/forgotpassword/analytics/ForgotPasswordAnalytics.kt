package com.tokopedia.session.forgotpassword.analytics

import com.tokopedia.track.TrackApp

class ForgotPasswordAnalytics {

    private val tracker = TrackApp.getInstance().gtm

    fun onViewForgotPassword() {

    }

    fun onSubmitClicked() {

    }

    fun onSuccessReset() {

    }

    fun onError(message: String) {

    }

    companion object {

    }
}