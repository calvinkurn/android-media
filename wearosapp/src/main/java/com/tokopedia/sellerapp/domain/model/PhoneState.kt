package com.tokopedia.sellerapp.domain.model

import com.tokopedia.sellerapp.presentation.screen.STATE

data class PhoneState(
    val stringState: String
) {
    fun getState(): STATE {
        return when(stringState) {
            STATE.SYNC.getStringState() -> STATE.SYNC
            STATE.COMPANION_NOT_INSTALLED.getStringState() -> STATE.COMPANION_NOT_INSTALLED
            STATE.COMPANION_NOT_LOGIN.getStringState() -> STATE.COMPANION_NOT_LOGIN
            STATE.COMPANION_NOT_REACHABLE.getStringState() -> STATE.COMPANION_NOT_REACHABLE
            else -> STATE.SYNC
        }
    }
}