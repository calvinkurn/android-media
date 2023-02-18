package com.tokopedia.sellerapp.domain.model

import com.tokopedia.utils.time.TimeHelper

data class NotificationModel(
    var notificationId: String = "",
    var readStatus: Int = 0,
    val status: Int = 0,
    val title: String = "",
    val shortDescription: String = "",
    val createTimeUnix: Long = 0L,
    val infoThumbnailUrl: String = ""
) {
    fun getTimeRelative(): String {
        return TimeHelper.getRelativeTimeFromNow(createTimeUnix * 1000)
    }
}
