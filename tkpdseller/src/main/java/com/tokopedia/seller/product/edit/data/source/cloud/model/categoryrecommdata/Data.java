package com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata;

/**
 * Created by Hendry on 4/18/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("product_category_prediction")
    @Expose
    private List<ProductCategoryPrediction> productCategoryPrediction = null;

    public List<ProductCategoryPrediction> getProductCategoryPrediction() {
        return productCategoryPrediction;
    }

    public void setProductCategoryPrediction(List<ProductCategoryPrediction> productCategoryPrediction) {
        this.productCategoryPrediction = productCategoryPrediction;
    }

}
