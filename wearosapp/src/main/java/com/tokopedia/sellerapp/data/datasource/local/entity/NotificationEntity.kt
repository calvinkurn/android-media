package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WearNotification")
data class NotificationEntity(
    @PrimaryKey @ColumnInfo(name = "notification_id")
    var notificationId: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "content")
    var content: String = "",
)
