package com.tokopedia.discovery.newdynamicfilter.database;

import android.content.Context;
import android.arch.persistence.room.Room;

public class FilterDatabaseClient {
    private static FilterDatabaseClient mInstance;

    private FilterDatabase filterDatabase;

    private FilterDatabaseClient(Context context) {
        filterDatabase = Room.databaseBuilder(context.getApplicationContext(),
                FilterDatabase.class, "Filter.db").fallbackToDestructiveMigration().build();
    }

    public static synchronized FilterDatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FilterDatabaseClient(context);
        }
        return mInstance;
    }

    public FilterDatabase getFilterDatabase() {
        return filterDatabase;
    }
}
