
package com.tokopedia.seller.product.imagepicker.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponseCatalogImage {

    @SerializedName("catalogs")
    @Expose
    private List<Catalog> catalogs = null;

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

}
