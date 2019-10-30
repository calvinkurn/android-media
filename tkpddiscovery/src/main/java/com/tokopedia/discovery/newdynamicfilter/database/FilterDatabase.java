package com.tokopedia.discovery.newdynamicfilter.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FilterDBModel.class}, version = 1)
public abstract class FilterDatabase extends RoomDatabase {
    public abstract FilterDao filterDao();
}
