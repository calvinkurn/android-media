package com.tokopedia.sellerapp.presentation.model

data class NotificationItem(
    val title: String = "",
    val unreadCount: Int = 0,
    val icon: Int = -1,
    val navigateToThePage: () -> Unit = {}
)