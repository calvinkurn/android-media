package com.tokopedia.discovery.intermediary.domain.model;

/**
 * Created by alifa on 3/24/17.
 */

public class ChildCategoryModel {

    private String categoryId = "";
    private String categoryName = "";
    private String categoryImageUrl = "";

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }
}
