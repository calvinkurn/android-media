package com.tokopedia.tkpdpdp.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpdpdp.viewmodel.ProductItem;

import java.util.List;

public class ResponseModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("products")
        @Expose
        private List<ProductItem> productItems;

        public List<ProductItem> getProductItems() {
            return productItems;
        }
    }
}
