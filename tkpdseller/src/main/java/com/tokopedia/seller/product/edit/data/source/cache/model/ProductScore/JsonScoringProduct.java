
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonScoringProduct {

    @SerializedName("data")
    @Expose
    private DataScoringProduct dataScoringProduct;

    public DataScoringProduct getDataScoringProduct() {
        return dataScoringProduct;
    }

    public void setDataScoringProduct(DataScoringProduct dataScoringProduct) {
        this.dataScoringProduct = dataScoringProduct;
    }

}
