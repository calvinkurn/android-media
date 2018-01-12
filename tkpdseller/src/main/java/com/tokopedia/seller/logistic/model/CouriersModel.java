package com.tokopedia.seller.logistic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouriersModel {

    @SerializedName("courier")
    @Expose
    private List<Courier> courier = null;

    public List<Courier> getCourier() {
        return courier;
    }

    public void setCourier(List<Courier> courier) {
        this.courier = courier;
    }
}
