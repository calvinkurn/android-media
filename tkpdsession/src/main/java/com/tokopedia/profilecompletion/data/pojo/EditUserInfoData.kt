package com.tokopedia.profilecompletion.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 7/3/17.
 */
data class EditUserInfoData (
    @SerializedName("is_success")
    @Expose
    var isSuccess: Int = 0
)