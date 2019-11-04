package com.tokopedia.discovery.newdynamicfilter.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FilterDBModel filterDBModel);

    @Query("SELECT * FROM FilterDBModel WHERE filter_id LIKE :query")
    FilterDBModel getFilterDataById(String query);
}
