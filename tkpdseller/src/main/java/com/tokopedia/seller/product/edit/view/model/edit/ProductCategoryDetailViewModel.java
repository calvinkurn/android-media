
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategoryDetailViewModel {

    @SerializedName("category_detail_id")
    @Expose
    private long categoryDetailId;
    @SerializedName("category_detail_name")
    @Expose
    private String categoryDetailName;
    @SerializedName("category_level")
    @Expose
    private long categoryLevel;
    @SerializedName("category_detail_breadcrumb_url")
    @Expose
    private String categoryDetailBreadcrumbUrl;

    public long getCategoryDetailId() {
        return categoryDetailId;
    }

    public void setCategoryDetailId(long categoryDetailId) {
        this.categoryDetailId = categoryDetailId;
    }

    public String getCategoryDetailName() {
        return categoryDetailName;
    }

    public void setCategoryDetailName(String categoryDetailName) {
        this.categoryDetailName = categoryDetailName;
    }

    public long getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(long categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getCategoryDetailBreadcrumbUrl() {
        return categoryDetailBreadcrumbUrl;
    }

    public void setCategoryDetailBreadcrumbUrl(String categoryDetailBreadcrumbUrl) {
        this.categoryDetailBreadcrumbUrl = categoryDetailBreadcrumbUrl;
    }

}
