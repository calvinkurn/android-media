package com.tokopedia.discovery.newdiscovery.util;

import android.text.TextUtils;

/**
 * Created by hangnadi on 10/3/17.
 */

public class SearchParameterBuilder {

    private String queryKey="";
    private String uniqueID;
    private String userID;
    private String departmentId;
    private int startRow;

    public SearchParameterBuilder setQueryKey(String queryKey) {
        this.queryKey = queryKey;
        return this;
    }

    public SearchParameterBuilder setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
        return this;
    }

    public SearchParameterBuilder setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public SearchParameterBuilder setStartRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public SearchParameterBuilder setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
        return this;
    }

    public SearchParameter build() {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setQueryKey(queryKey);
        searchParameter.setUniqueID(uniqueID);
        searchParameter.setUserID(userID);
        searchParameter.setStartRow(startRow);
        searchParameter.setDepartmentId(departmentId);
        return searchParameter;
    }

    public static SearchParameterBuilder createInstance() {
        return new SearchParameterBuilder();
    }

}
