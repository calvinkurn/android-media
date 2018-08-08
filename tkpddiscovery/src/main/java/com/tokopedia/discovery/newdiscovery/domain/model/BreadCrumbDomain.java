package com.tokopedia.discovery.newdiscovery.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 10/12/17.
 */

public class BreadCrumbDomain {
    private String ID;
    private String name;
    private String url;
    private String identifier;
    private String parentID;
    private String treeCount;
    private List<BreadCrumbDomain> child;

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

    public void setTreeCount(String treeCount) {
        this.treeCount = treeCount;
    }

    public String getTreeCount() {
        return treeCount;
    }

    public void setChild(List<BreadCrumbDomain> child) {
        this.child = child;
    }

    public List<BreadCrumbDomain> getChild() {
        return child;
    }
}
