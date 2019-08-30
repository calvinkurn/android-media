package com.tokopedia.transaction.common.data.ticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitHelpTicketGqlResponse(
        @SerializedName("submit_help_ticket")
        @Expose
        val submitHelpTicketResponse: SubmitHelpTicketResponse
)