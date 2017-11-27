package com.tokopedia.inbox.inboxmessageold.util;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.model.InboxMessageDetailModelDB;
import com.tokopedia.core.database.model.InboxMessageDetailModelDB_Table;

/**
 * Created by Nisie on 5/23/16.
 */
public class InboxMessageCacheManager {

    private String messageId;
    private String data;
    private long expiredTime = 0;
    private static String TAG = "InboxMessageCacheManager";

    public InboxMessageCacheManager() {

    }

    public InboxMessageCacheManager setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public InboxMessageCacheManager setData(String data) {
        this.data = data;
        return this;
    }

    public InboxMessageCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public void save() {
        InboxMessageDetailModelDB cache = new InboxMessageDetailModelDB();
        cache.id = messageId;
        cache.data = data;
        cache.expiredTime = this.expiredTime;
        cache.save();
    }

    public String getCache(String reviewId) throws RuntimeException {
        try {
            InboxMessageDetailModelDB cache = new Select().from(InboxMessageDetailModelDB.class)
                    .where(InboxMessageDetailModelDB_Table.id.is(reviewId))
                    .querySingle();

            if (isCacheExpired(cache.expiredTime)) {
                throw new RuntimeException("Cache is expired");
            } else {
                return cache.data;
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("Cache is expired");
        }
    }

    private Boolean isCacheExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }

    public void delete(String key) {
        new Delete().from(InboxMessageDetailModelDB.class).where(InboxMessageDetailModelDB_Table.id.is(key)).execute();
    }

    public void deleteAll() {
        new Delete().from(InboxMessageDetailModelDB.class).execute();
    }
}