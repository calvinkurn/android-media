package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WearNotification")
data class NotificationEntity(
    @PrimaryKey @ColumnInfo(name = "notification_id")
    var notificationId: String = "",
    @ColumnInfo(name = "read_status")
    var readStatus: Int = 0,
    @ColumnInfo(name = "status")
    val status: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "short_description")
    val shortDescription: String = "",
    @ColumnInfo(name = "create_time_unix")
    val createTimeUnix: Long = 0L,
    @ColumnInfo(name = "info_thumbnail_url")
    val infoThumbnailUrl: String = ""
)
