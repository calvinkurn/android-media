package com.tokopedia.discovery.newdynamicfilter.helper;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.FilterModelDB;
import com.tokopedia.core.database.model.FilterModelDB_Table;

import java.util.List;

/**
 * Created by nakama on 11/23/17.
 */

public class DynamicFilterDbManager implements DbFlowOperation<FilterModelDB> {

    private String filterID;
    private String filterData;

    public String getFilterID() {
        return filterID;
    }

    public void setFilterID(String filterID) {
        this.filterID = filterID;
    }

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    @Override
    public void store() {
        FilterModelDB cache = new FilterModelDB();
        cache.filterId = getFilterID();
        cache.filterData = getFilterData();
        cache.save();
    }

    @Override
    public void store(FilterModelDB data) {

    }

    @Override
    public void delete(String key) {
        deleteAll();
    }

    @Override
    public void deleteAll() {
        new Delete().from(FilterModelDB.class).execute();
    }

    @Override
    public boolean isExpired(long time) {
        return true;
    }

    @Override
    public FilterModelDB getData(String key) {
        return null;
    }

    @Override
    public List<FilterModelDB> getDataList(String key) {
        return null;
    }

    @Override
    public String getValueString(String key) {
        FilterModelDB cache = new Select().from(FilterModelDB.class)
                .where(FilterModelDB_Table.filterId.is(key))
                .querySingle();
        return cache != null ? cache.filterData : null;
    }

    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        Gson gson = new Gson();
        return (Z) gson.fromJson(getValueString(key), clazz);
    }
}
