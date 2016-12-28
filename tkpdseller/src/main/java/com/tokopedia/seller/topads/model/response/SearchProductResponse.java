package com.tokopedia.seller.topads.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.Meta;
import com.tokopedia.seller.topads.model.data.Page;
import com.tokopedia.seller.topads.model.data.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class SearchProductResponse {

    @SerializedName("data")
    @Expose
    private List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> data) {
        this.productList = data;
    }
}