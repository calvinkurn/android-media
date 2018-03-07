package com.tokopedia.inbox.attachproduct.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.shopinfo.models.productmodel.Product;

import java.util.List;

/**
 * Created by Hendri on 02/03/18.
 */

public class AttachProductAPIResponseWrapper {
    @SerializedName("list")
    @Expose
    List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
