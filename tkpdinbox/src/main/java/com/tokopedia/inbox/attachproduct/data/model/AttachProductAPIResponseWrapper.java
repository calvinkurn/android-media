package com.tokopedia.inbox.attachproduct.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendri on 02/03/18.
 */

public class AttachProductAPIResponseWrapper {
    @SerializedName("list")
    @Expose
    private List<DataProductResponse> products;

    public List<DataProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<DataProductResponse> products) {
        this.products = products;
    }
}
