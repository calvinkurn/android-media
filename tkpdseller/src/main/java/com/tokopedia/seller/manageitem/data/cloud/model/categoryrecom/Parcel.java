package com.tokopedia.seller.manageitem.data.cloud.model.categoryrecom;

/**
 * Created by Hendry on 4/18/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parcel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Parcel(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
