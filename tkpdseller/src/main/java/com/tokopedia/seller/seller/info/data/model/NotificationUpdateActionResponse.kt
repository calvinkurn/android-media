package com.tokopedia.seller.seller.info.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationUpdateActionResponse(
        @Expose
        @SerializedName("message_error")
        var messageError: String = "",
        @Expose
        @SerializedName("data")
        var data: NotificationUpdateActionIsSuccess = NotificationUpdateActionIsSuccess()

)

data class NotificationUpdateActionIsSuccess(
        @Expose
        @SerializedName("is_success")
        var isSuccess: String = ""
)