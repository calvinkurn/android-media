package com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata;

/**
 * Created by Hendry on 4/18/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryRecommDataModel {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

}
