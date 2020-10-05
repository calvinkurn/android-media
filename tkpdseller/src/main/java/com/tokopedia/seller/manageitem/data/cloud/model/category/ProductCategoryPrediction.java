package com.tokopedia.seller.manageitem.data.cloud.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendry on 4/18/2017.
 */

public class ProductCategoryPrediction {

    @SerializedName("confidence_score")
    @Expose
    private double confidenceScore;
    @SerializedName("product_category_id")
    @Expose
    private List<ProductCategoryId> productCategoryId = null;

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public List<ProductCategoryId> getProductCategoryId() {
        return productCategoryId;
    }

}
