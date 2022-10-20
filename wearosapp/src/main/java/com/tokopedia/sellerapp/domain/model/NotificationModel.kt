package com.tokopedia.sellerapp.domain.model

data class NotificationModel(
    var notificationId: String = "",
    var readStatus: Int = 0,
    val status: Int = 0,
    val title: String = "",
    val shortDescription: String = ""
)
