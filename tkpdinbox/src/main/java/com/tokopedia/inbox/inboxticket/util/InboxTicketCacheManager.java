package com.tokopedia.inbox.inboxticket.util;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.model.InboxTicketDetailModelDB;
import com.tokopedia.core.database.model.InboxTicketDetailModelDB_Table;

/**
 * Created by nisie on 10/6/16.
 */

public class InboxTicketCacheManager {

    private String ticketId;
    private String data;
    private long expiredTime = 0;
    private static String TAG = "InboxTicketCacheManager";

    public InboxTicketCacheManager() {

    }

    public InboxTicketCacheManager setTicketId(String ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public InboxTicketCacheManager setData(String data) {
        this.data = data;
        return this;
    }

    public InboxTicketCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public void save() {
        InboxTicketDetailModelDB cache = new InboxTicketDetailModelDB();
        cache.id = ticketId;
        cache.data = data;
        cache.expiredTime = this.expiredTime;
        cache.save();
    }

    public String getCache(String ticketId) throws RuntimeException {
        try {
            InboxTicketDetailModelDB cache = new Select().from(InboxTicketDetailModelDB.class)
                    .where(InboxTicketDetailModelDB_Table.id.is(ticketId))
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
        new Delete().from(InboxTicketDetailModelDB.class).where(InboxTicketDetailModelDB_Table.id.is(key)).execute();
    }

    public void deleteAll() {
        new Delete().from(InboxTicketDetailModelDB.class).execute();
    }

}
