package com.tokopedia.discovery.newdynamicfilter.database;

import java.io.Serializable;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FilterDBModel implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "filter_id")
    private String filterId;

    @ColumnInfo(name = "filter_data")
    private String filterData;

    public FilterDBModel(String filterId, String filterData) {
        this.filterId = filterId;
        this.filterData = filterData;
    }

    public String getFilterId() {
        return filterId;
    }

    public String getFilterData() {
        return filterData;
    }
}
