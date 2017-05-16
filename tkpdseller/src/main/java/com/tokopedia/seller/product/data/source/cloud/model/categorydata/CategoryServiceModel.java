
package com.tokopedia.seller.product.data.source.cloud.model.categorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryServiceModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
