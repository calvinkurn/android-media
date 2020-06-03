package com.tokopedia.profilecompletion.view.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by nisie on 8/2/17.
 */

@Parcelize
data class ProfileCompletionViewModel (
        var gender: Int = 0,
        var phone: String = "",
        var bday: String = "",
        var completion: Int = 0,
        var isPhoneVerified: Boolean = false
) : Parcelable