package com.tokopedia.transaction.common.data.ticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitTicketTextResponse(
        @SerializedName("submit_title")
        @Expose
        val submitTitle: String = "",

        @SerializedName("submit_description")
        @Expose
        val submitDescription: String = "",

        @SerializedName("success_button")
        @Expose
        val successButton: String = ""
)