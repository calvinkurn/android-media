package com.tokopedia.discovery.newdynamicfilter.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {FilterDBModel.class}, version = 1)
public abstract class FilterDatabase extends RoomDatabase {
    public abstract FilterDao filterDao();
}
