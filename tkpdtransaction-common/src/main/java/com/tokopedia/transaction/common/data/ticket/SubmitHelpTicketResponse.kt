package com.tokopedia.transaction.common.data.ticket

data class SubmitHelpTicketResponse(
        val status: String = "",
        val errorMessage: ArrayList<String> = arrayListOf(),
        val data: SubmitTicketDataResponse = SubmitTicketDataResponse()
)