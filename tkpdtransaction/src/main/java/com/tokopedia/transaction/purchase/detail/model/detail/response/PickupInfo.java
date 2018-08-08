package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 09/01/18.
 */

public class PickupInfo {
    @SerializedName("name")
    @Expose
    private String name;

    public PickupInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}