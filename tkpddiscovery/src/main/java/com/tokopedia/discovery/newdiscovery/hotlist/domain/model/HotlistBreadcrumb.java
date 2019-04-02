package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistBreadcrumb {
    private String ID;
    private String name;
    private String url;
    private String identifier;
    private String parentID;
    private int treeCount;
    private String totalData;
    private List<HotlistBreadcrumb> child;

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setTreeCount(int treeCount) {
        this.treeCount = treeCount;
    }

    public int getTreeCount() {
        return treeCount;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }

    public String getTotalData() {
        return totalData;
    }

    public void setChild(List<HotlistBreadcrumb> child) {
        this.child = child;
    }

    public List<HotlistBreadcrumb> getChild() {
        return child;
    }
}
