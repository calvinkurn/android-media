package com.tokopedia.transaction.common.data.ticket

data class SubmitTicketDataResponse(
        val success: Int = 0,
        val message: ArrayList<String> = arrayListOf()
)