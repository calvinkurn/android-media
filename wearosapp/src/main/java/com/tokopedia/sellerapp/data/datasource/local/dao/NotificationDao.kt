package com.tokopedia.sellerapp.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.sellerapp.data.datasource.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationList(list: List<NotificationEntity>)

    @Query("SELECT * FROM WearNotification")
    fun getNotificationList(): Flow<List<NotificationEntity>>

    @Transaction
    @Query("SELECT * FROM WearNotification wn WHERE wn.notification_id IS (:notificationId)")
    fun getNotificationById(notificationId: String): Flow<NotificationEntity>
}