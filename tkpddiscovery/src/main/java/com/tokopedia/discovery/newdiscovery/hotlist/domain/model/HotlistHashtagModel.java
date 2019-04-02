package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistHashtagModel {
    private String departmentID;
    private String name;
    private String URL;

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }
}
