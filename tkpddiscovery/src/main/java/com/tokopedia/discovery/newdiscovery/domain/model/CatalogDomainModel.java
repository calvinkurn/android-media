package com.tokopedia.discovery.newdiscovery.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogDomainModel {
    private List<BreadCrumbDomain> breadCrumbList;
    private String shareURL;
    private String uriNext;
    private List<CatalogItem> catalogList;

    public void setBreadCrumbList(List<BreadCrumbDomain> breadCrumbList) {
        this.breadCrumbList = breadCrumbList;
    }

    public List<BreadCrumbDomain> getBreadCrumbList() {
        return breadCrumbList;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public String getShareURL() {
        return shareURL;
    }

    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    public String getUriNext() {
        return uriNext;
    }

    public void setCatalogList(List<CatalogItem> catalogList) {
        this.catalogList = catalogList;
    }

    public List<CatalogItem> getCatalogList() {
        return catalogList;
    }
}
