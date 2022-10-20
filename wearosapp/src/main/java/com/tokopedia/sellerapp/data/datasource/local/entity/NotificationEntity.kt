package com.tokopedia.sellerapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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
    val shortDescription: String = ""
)
