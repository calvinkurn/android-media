package com.tokopedia.transaction.pickuppoint.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointResponseEntity {
    @SerializedName("data")
    @Expose
    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }
}
