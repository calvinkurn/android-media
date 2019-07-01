package com.tokopedia.discovery.newdynamicfilter.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FilterDBModel filterDBModel);

    @Query("SELECT * FROM FilterDBModel WHERE filter_id LIKE :query")
    FilterDBModel getFilterDataById(String query);
}
