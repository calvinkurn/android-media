
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCatalogViewModel {

    @SerializedName("catalog_id")
    @Expose
    private long catalogId;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;
    @SerializedName("catalog_url")
    @Expose
    private String catalogUrl;
    @SerializedName("catalog_status")
    @Expose
    private long catalogStatus;

    public long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    public long getCatalogStatus() {
        return catalogStatus;
    }

    public void setCatalogStatus(long catalogStatus) {
        this.catalogStatus = catalogStatus;
    }

}
