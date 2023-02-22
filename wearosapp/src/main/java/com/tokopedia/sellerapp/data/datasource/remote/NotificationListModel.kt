package com.tokopedia.sellerapp.data.datasource.remote

import com.google.gson.annotations.SerializedName

data class NotificationListModel(
    @SerializedName("notifications")
    val notifications: List<Notification> = listOf()
) {
    data class Notification(
        @SerializedName("notif_id")
        val notificationId: String = "",
        @SerializedName("read_status")
        var readStatus: Int = 0,
        @SerializedName("short_description")
        val shortDescription: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("create_time_unix")
        val createTimeUnix: Long = 0L,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("data_notification")
        val dataNotification: DataNotification = DataNotification()
    )

    data class DataNotification(
        @SerializedName("info_thumbnail_url")
        val infoThumbnailUrl: String = ""
    )
}
