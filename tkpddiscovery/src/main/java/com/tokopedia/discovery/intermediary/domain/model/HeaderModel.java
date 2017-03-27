package com.tokopedia.discovery.intermediary.domain.model;

/**
 * Created by alifa on 3/24/17.
 */

public class HeaderModel {

    private String categoryName="";
    private String headerImageUrl="";

    public HeaderModel(String categoryName, String headerImageUrl) {
        this.categoryName = categoryName;
        this.headerImageUrl = headerImageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }
}
