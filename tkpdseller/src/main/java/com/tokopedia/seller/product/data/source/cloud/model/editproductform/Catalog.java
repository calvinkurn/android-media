
package com.tokopedia.seller.product.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog {

    @SerializedName("catalog_id")
    @Expose
    private int catalogId;
    @SerializedName("catalog_name")
    @Expose
    private int catalogName;

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public int getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(int catalogName) {
        this.catalogName = catalogName;
    }

}
