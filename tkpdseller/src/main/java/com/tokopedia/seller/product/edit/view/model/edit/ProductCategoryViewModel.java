
package com.tokopedia.seller.product.edit.view.model.edit;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategoryViewModel {

    @SerializedName("category_id")
    @Expose
    private long categoryId;
    @SerializedName("category_full_name")
    @Expose
    private String categoryFullName;
    @SerializedName("category_full_titile")
    @Expose
    private String categoryFullTitile;
    @SerializedName("category_breadcrumb_url")
    @Expose
    private String categoryBreadcrumbUrl;
    @SerializedName("category_detail")
    @Expose
    private List<ProductCategoryDetailViewModel> categoryDetail = new ArrayList<>();

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryFullName() {
        return categoryFullName;
    }

    public void setCategoryFullName(String categoryFullName) {
        this.categoryFullName = categoryFullName;
    }

    public String getCategoryFullTitile() {
        return categoryFullTitile;
    }

    public void setCategoryFullTitile(String categoryFullTitile) {
        this.categoryFullTitile = categoryFullTitile;
    }

    public String getCategoryBreadcrumbUrl() {
        return categoryBreadcrumbUrl;
    }

    public void setCategoryBreadcrumbUrl(String categoryBreadcrumbUrl) {
        this.categoryBreadcrumbUrl = categoryBreadcrumbUrl;
    }

    public List<ProductCategoryDetailViewModel> getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(List<ProductCategoryDetailViewModel> categoryDetail) {
        this.categoryDetail = categoryDetail;
    }

}
