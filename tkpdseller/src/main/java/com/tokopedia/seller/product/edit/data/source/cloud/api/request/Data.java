package com.tokopedia.seller.product.edit.data.source.cloud.api.request;

/**
 * Created by Hendry on 4/18/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("product_title")
    @Expose
    private String productTitle;

    public Data(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

}
