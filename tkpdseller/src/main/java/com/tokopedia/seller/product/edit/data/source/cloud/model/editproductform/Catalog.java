
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog {

    @SerializedName("catalog_id")
    @Expose
    private long catalogId;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;

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

}
