package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class OrderResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("openAmount")
    private int openAmount;

    @SerializedName("shippingPrices")
    private int shippingPrices;

    public int getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(int openAmount) {
        this.openAmount = openAmount;
    }

    public int getShippingPrices() {
        return shippingPrices;
    }

    public void setShippingPrices(int shippingPrices) {
        this.shippingPrices = shippingPrices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
